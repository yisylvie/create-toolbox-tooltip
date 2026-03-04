package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.misterpemodder.shulkerboxtooltip.api.PreviewType;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.VanillaPreviewRenderer;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

@Mixin(VanillaPreviewRenderer.class)
public abstract class VanillaPreviewRendererMixin extends BasePreviewRenderer{
    protected VanillaPreviewRendererMixin(int slotWidth, int slotHeight, int slotXOffset, int slotYOffset) {
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
//		remap = false
    )
    private void createtoolboxtooltip$dontDrawIfEmpty(
            int x, int y, GuiGraphics context, Font textRenderer,
            int mouseX, int mouseY, CallbackInfo ci) {
        if (this.provider instanceof ToolboxPreviewProvider) {
            ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider)this.provider;
            if(this.previewType == PreviewType.COMPACT
                    && ToolboxPreviewProvider.getItemCount(toolboxProvider.getInventory(this.previewContext)) == 0) {
                ci.cancel();
            }
        }
    }
}
