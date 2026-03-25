package com.yisylvie.createtoolboxtooltip.neoforge;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;

import com.misterpemodder.shulkerboxtooltip.api.neoforge.ShulkerBoxTooltipPlugin;

import com.yisylvie.createtoolboxtooltip.createToolboxTooltip;

@Mod(createToolboxTooltip.ID)
public final class createToolboxTooltipImpl {
    public createToolboxTooltipImpl() {
		// plugin registration
		// ModLoadingContext.get().registerExtensionPoint(ShulkerBoxTooltipPlugin.class,
		// 	() -> new ShulkerBoxTooltipPlugin(createToolboxTooltipPlugin::new));
	}
}
