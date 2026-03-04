package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;
import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.impl.tooltip.PreviewTooltipComponent;
import com.misterpemodder.shulkerboxtooltip.api.PreviewType;
import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import com.misterpemodder.shulkerboxtooltip.impl.config.Configuration.PreviewPosition;

@Mixin(PreviewTooltipComponent.class)
public abstract class PreviewTooltipComponentMixin {

    @Shadow(remap = false)
    private PreviewProvider provider;

    @Shadow(remap = false)
    private PreviewContext context;

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
        cancellable = true
//		remap = false
    )
    private void createtoolboxtooltip$changeHeight(CallbackInfoReturnable<Integer> cir) {
        if (this.provider instanceof ToolboxPreviewProvider) {
            ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider)this.provider;
            if (ShulkerBoxTooltip.config.preview.position == PreviewPosition.INSIDE
                    && ShulkerBoxTooltipApi.getCurrentPreviewType(this.provider.isFullPreviewAvailable(this.context))
                    == PreviewType.COMPACT
                    && toolboxProvider.isInventoryEmpty(this.context)) {
                cir.setReturnValue(0);
            }
        }
    }
}
