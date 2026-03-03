package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.access.PreviewCategoryAccess;

import org.spongepowered.asm.mixin.Mixin;

import com.misterpemodder.shulkerboxtooltip.impl.config.Configuration;
import com.misterpemodder.shulkerboxtooltip.impl.config.annotation.AutoTooltip;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import org.spongepowered.asm.mixin.Unique;

@Mixin(Configuration.PreviewCategory.class)
 public abstract class PreviewCategoryMixin implements PreviewCategoryAccess {

	@Unique
	@AutoTooltip
	@Comment("""
    	Turn off all of the stuffs besides toolboxes to prevent incompatibilities with other mods.
   		""")
    public boolean createtoolboxtooltip$enableOnlyToolboxes = false;

	@Unique
	public boolean createtoolboxtooltip$getEnableOnlyToolboxes() {
		return createtoolboxtooltip$enableOnlyToolboxes;
	}
}
