package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;
import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.PreviewType;
import com.misterpemodder.shulkerboxtooltip.impl.config.Configuration;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.VanillaPreviewRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

@SuppressWarnings("UnstableApiUsage")
@Mixin(VanillaPreviewRenderer.class)
public abstract class VanillaPreviewRendererMixin extends BasePreviewRenderer{
    protected VanillaPreviewRendererMixin(
			int slotWidth, int slotHeight, int slotXOffset, int slotYOffset) {
        super(slotWidth, slotHeight, slotXOffset, slotYOffset);
    }

    /**
     * Vanilla theme displays even if inventory is empty, so we must override for
     * when there are nonempty compartments and the preview type is compact
     */
    @Inject(
        method = "draw",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void createtoolboxtooltip$dontDrawIfEmpty(
            int x, int y, GuiGraphics context, Font textRenderer,
            int mouseX, int mouseY, CallbackInfo ci) {
        if (this.provider instanceof ToolboxPreviewProvider toolboxProvider) {
			if(this.previewType == PreviewType.COMPACT
                    && ToolboxPreviewProvider.getItemCount(
							toolboxProvider.getInventory(this.previewContext)) == 0) {
                ci.cancel();
            }
        }
    }

	/**
	 * We want tooltip hints but not the inventory to render when the preview
	 * type is compact and isInventoryEmpty(). Doing so causes there to
	 * be a silly little gap where the inventory would have been displayed.
	 * In this instance, we must override getHeight() to close that gap.
	 */
	@Inject(
			method = "getHeight",
			at = @At(
					value = "RETURN"
			),
			cancellable = true,
			remap = false
	)
	private void createtoolboxtooltip$changeHeight(CallbackInfoReturnable<Integer> cir) {
		if (this.provider instanceof ToolboxPreviewProvider toolboxProvider) {
			if (ShulkerBoxTooltip.config.preview.position
					== Configuration.PreviewPosition.INSIDE
					&& ShulkerBoxTooltipApi.getCurrentPreviewType(
							this.provider.isFullPreviewAvailable(this.previewContext))
					== PreviewType.COMPACT
					&& toolboxProvider.isInventoryEmpty(this.previewContext)) {
				cir.setReturnValue(0);
			}
		}
	}
}
