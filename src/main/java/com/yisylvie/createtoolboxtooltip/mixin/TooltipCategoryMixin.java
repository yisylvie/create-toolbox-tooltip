package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.access.TooltipCategoryAccess;

import com.misterpemodder.shulkerboxtooltip.impl.config.Configuration;
import com.misterpemodder.shulkerboxtooltip.impl.config.annotation.AutoTooltip;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import blue.endless.jankson.Comment;

@SuppressWarnings("UnstableApiUsage")
@Mixin(Configuration.TooltipCategory.class)
 public abstract class TooltipCategoryMixin implements TooltipCategoryAccess {

	/**
	 * adds a new option to Shulker Box Tooltip's config
 	 */
	@Unique
	@AutoTooltip
	@Comment("""
			If on, hides Create's default tooltip for toolboxes.
			\n(This does nothing if the Create Client Setting
			\n"Enable Tooltips" is off.)""")
    public boolean createtoolboxtooltip$hideCreateTooltips = false;

	@Unique
	public boolean createtoolboxtooltip$getHideCreateTooltips() {
		return createtoolboxtooltip$hideCreateTooltips;
	}
}
