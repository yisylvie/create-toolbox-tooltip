package com.yisylvie.createtoolboxtooltip;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class createToolboxTooltip implements ModInitializer {
	public static final String ID = "createtoolboxtooltip";
	public static final String NAME = "Create Toolbox Tooltip";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	@Override
	public void onInitialize() {
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(ID, path);
	}
}