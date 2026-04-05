package com.yisylvie.createtoolboxtooltip;

import com.misterpemodder.shulkerboxtooltip.api.neoforge.ShulkerBoxTooltipPlugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;

import net.minecraft.resources.ResourceLocation;

@Mod(createToolboxTooltip.ID)
public final class createToolboxTooltip {
    public static final String ID = "createtoolboxtooltip";
    public static final String NAME = "Create Toolbox Tooltip";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	public createToolboxTooltip() {
		// plugin registration
		ModLoadingContext.get().registerExtensionPoint(ShulkerBoxTooltipPlugin.class,
			() -> new ShulkerBoxTooltipPlugin(createToolboxTooltipPlugin::new));
	}
}
