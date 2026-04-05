package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import com.misterpemodder.shulkerboxtooltip.api.PreviewType;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.ModPreviewRenderer;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ModPreviewRenderer.class)
public abstract class ModPreviewRendererMixin extends BasePreviewRenderer{
	protected ModPreviewRendererMixin(
			int slotWidth, int slotHeight, int slotXOffset, int slotYOffset) {
		super(slotWidth, slotHeight, slotXOffset, slotYOffset);
	}

	/**
	 * Mod default theme doesn't display if inventory is empty,
	 * so we must override for when there are nonempty compartments
	 * and the preview type is full
	 */
	@ModifyExpressionValue(
			method = "draw",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;isEmpty()Z",
					ordinal = 0
			)
	)
	private boolean createtoolboxtooltip$drawIfEmpty(boolean isEmpty) {
		if (this.provider instanceof ToolboxPreviewProvider toolboxProvider) {
			if(this.previewType == PreviewType.FULL) {
				// We must (double) negate since isEmpty is negated in original if statement
				return !toolboxProvider.shouldDisplay(this.previewContext);
			}
		}
		return isEmpty;
	}
}
