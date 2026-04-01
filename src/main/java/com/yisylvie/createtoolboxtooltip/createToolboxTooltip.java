package com.yisylvie.createtoolboxtooltip;

import com.misterpemodder.shulkerboxtooltip.api.neoforge.ShulkerBoxTooltipPlugin;

import net.minecraft.resources.ResourceLocation;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(createToolboxTooltip.ID)
public final class createToolboxTooltip {
    public static final String ID = "createtoolboxtooltip";
    public static final String NAME = "Create Toolbox Tooltip";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

	public createToolboxTooltip() {
		// plugin registration
		ModLoadingContext.get().registerExtensionPoint(ShulkerBoxTooltipPlugin.class,
			() -> new ShulkerBoxTooltipPlugin(createToolboxTooltipPlugin::new));
	}
}
