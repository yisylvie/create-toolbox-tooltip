package com.yisylvie.createtoolboxtooltip.mixin;

import com.misterpemodder.shulkerboxtooltip.api.PreviewType;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(PreviewType.class)
public abstract class PreviewTypeMixin {
	/**
	 * To display the preview type nicely in the config menu.
	 */
	@Override
	public String toString() {
		return "shulkerboxtooltip.config.preview_type." + ((PreviewType)(Object)this).name().toLowerCase();
	}
}
