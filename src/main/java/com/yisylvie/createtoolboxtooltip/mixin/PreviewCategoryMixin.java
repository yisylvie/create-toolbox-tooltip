package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.access.PreviewCategoryAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.misterpemodder.shulkerboxtooltip.impl.config.Configuration;
import com.misterpemodder.shulkerboxtooltip.impl.config.annotation.AutoTooltip;

import blue.endless.jankson.Comment;

@SuppressWarnings("UnstableApiUsage")
@Mixin(Configuration.PreviewCategory.class)
 public abstract class PreviewCategoryMixin implements PreviewCategoryAccess {

	/**
	 * adds a new option to Shulker Box Tooltip's config
 	 */
	@Unique
	@AutoTooltip
	@Comment("""
		Turn off every preview type besides toolboxes
		to prevent incompatibilities with other mods""")
    public boolean createtoolboxtooltip$enableOnlyToolboxes = false;

	@Unique
	public boolean createtoolboxtooltip$getEnableOnlyToolboxes() {
		return createtoolboxtooltip$enableOnlyToolboxes;
	}
}
