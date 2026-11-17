package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.access.PreviewCategoryAccess;

import com.misterpemodder.shulkerboxtooltip.api.PreviewType;
import com.misterpemodder.shulkerboxtooltip.impl.config.Configuration;
import com.misterpemodder.shulkerboxtooltip.impl.config.annotation.AutoTooltip;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import blue.endless.jankson.Comment;

/**
 * Adds new options to Shulker Box Tooltip's preview config.
 */
@SuppressWarnings("UnstableApiUsage")
@Mixin(Configuration.PreviewCategory.class)
 public abstract class PreviewCategoryMixin implements PreviewCategoryAccess {

	@Unique
	@AutoTooltip
	@Comment("""
		Toggles inventory toolbox preview.""")
	public boolean createtoolboxtooltip$enableToolboxPreviews = true;

	@Unique
	public boolean createtoolboxtooltip$getEnableToolboxPreviews() {
		return createtoolboxtooltip$enableToolboxPreviews;
	}

	@Unique
	@AutoTooltip
	@Comment("""
		Sets the tooltip preview mode in Create's radial toolbox menu.""")
	public PreviewType createtoolboxtooltip$radialToolboxMenuTooltip = PreviewType.FULL;

	@Unique
	public PreviewType createtoolboxtooltip$getRadialToolboxMenuTooltip() {
		return createtoolboxtooltip$radialToolboxMenuTooltip;
	}

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
