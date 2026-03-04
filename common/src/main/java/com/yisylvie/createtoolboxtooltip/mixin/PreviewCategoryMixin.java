package com.yisylvie.createtoolboxtooltip.mixin;

import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import com.misterpemodder.shulkerboxtooltip.impl.provider.PreviewProviderRegistryImpl;
import com.simibubi.create.Create;
import com.yisylvie.createtoolboxtooltip.access.PreviewCategoryAccess;

import com.yisylvie.createtoolboxtooltip.createToolboxTooltip;

import me.shedaniel.clothconfig2.api.ConfigCategory;

import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;

import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;
import com.misterpemodder.shulkerboxtooltip.impl.config.Configuration;
import com.misterpemodder.shulkerboxtooltip.impl.config.annotation.AutoTooltip;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import org.spongepowered.asm.mixin.Unique;

@Mixin(Configuration.PreviewCategory.class)
 public abstract class PreviewCategoryMixin implements PreviewCategoryAccess {

	@Unique
	@AutoTooltip
	@Comment("""
   		Turn off every preview type besides toolboxes to prevent incompatibilities with other mods
   		""")
    public boolean createtoolboxtooltip$enableOnlyToolboxes = false;

	@Unique
	public boolean createtoolboxtooltip$getEnableOnlyToolboxes() {
		return createtoolboxtooltip$enableOnlyToolboxes;
	}
}
