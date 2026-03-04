package com.yisylvie.createtoolboxtooltip;

import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry;

import com.misterpemodder.shulkerboxtooltip.impl.provider.PreviewProviderRegistryImpl;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;

// import java.lang.module.Configuration;
import java.lang.reflect.Field;
import java.security.Provider;
import java.util.Iterator;
import java.util.Set;

import com.misterpemodder.shulkerboxtooltip.impl.config.Configuration;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

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
