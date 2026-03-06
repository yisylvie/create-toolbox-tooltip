package com.yisylvie.createtoolboxtooltip;

import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import java.util.Iterator;
import javax.annotation.Nonnull;

import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorKey;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorRegistry;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry;

import com.tterrag.registrate.util.entry.BlockEntry;

import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.simibubi.create.AllBlocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

public class createToolboxTooltipPlugin implements ShulkerBoxTooltipApi {
    private static final Item[] TOOLBOX_ITEMS;
    private static final ResourceLocation toolboxResourceLocation = new ResourceLocation("create", "toolboxes");

    @Override
    public void registerProviders(@Nonnull PreviewProviderRegistry registry) {
        registry.register(toolboxResourceLocation, new ToolboxPreviewProvider(), TOOLBOX_ITEMS);
    }

    /**
     * Gives our toolboxes their own options for colors in the Mod Menu config
     * Has to use a copy of the shulkers' color keys otherwise they get linked to the shulker colors
     */
    @Override
    @Environment(EnvType.CLIENT)
    public void registerColors(@Nonnull ColorRegistry registry) {
        registry.category(toolboxResourceLocation)
            .register(ColorKey.copyOf(ColorKey.WHITE_SHULKER_BOX), "white_toolbox", blockName("white_toolbox"))
            .register(ColorKey.copyOf(ColorKey.ORANGE_SHULKER_BOX), "orange_toolbox", blockName("orange_toolbox"))
            .register(ColorKey.copyOf(ColorKey.MAGENTA_SHULKER_BOX), "magenta_toolbox", blockName("magenta_toolbox"))
            .register(ColorKey.copyOf(ColorKey.LIGHT_BLUE_SHULKER_BOX), "light_blue_toolbox", blockName("light_blue_toolbox"))
            .register(ColorKey.copyOf(ColorKey.YELLOW_SHULKER_BOX), "yellow_toolbox", blockName("yellow_toolbox"))
            .register(ColorKey.copyOf(ColorKey.LIME_SHULKER_BOX), "lime_toolbox", blockName("lime_toolbox"))
            .register(ColorKey.copyOf(ColorKey.PINK_SHULKER_BOX), "pink_toolbox", blockName("pink_toolbox"))
            .register(ColorKey.copyOf(ColorKey.GRAY_SHULKER_BOX), "gray_toolbox", blockName("gray_toolbox"))
            .register(ColorKey.copyOf(ColorKey.LIGHT_GRAY_SHULKER_BOX), "light_gray_toolbox", blockName("light_gray_toolbox"))
            .register(ColorKey.copyOf(ColorKey.CYAN_SHULKER_BOX), "cyan_toolbox", blockName("cyan_toolbox"))
            .register(ColorKey.copyOf(ColorKey.PURPLE_SHULKER_BOX), "purple_toolbox", blockName("purple_toolbox"))
            .register(ColorKey.copyOf(ColorKey.BLUE_SHULKER_BOX), "blue_toolbox", blockName("blue_toolbox"))
            .register(ColorKey.copyOf(ColorKey.BROWN_SHULKER_BOX), "brown_toolbox", blockName("brown_toolbox"))
            .register(ColorKey.copyOf(ColorKey.GREEN_SHULKER_BOX), "green_toolbox", blockName("green_toolbox"))
            .register(ColorKey.copyOf(ColorKey.RED_SHULKER_BOX), "red_toolbox", blockName("red_toolbox"))
            .register(ColorKey.copyOf(ColorKey.BLACK_SHULKER_BOX), "black_toolbox", blockName("black_toolbox"));
    }

    private static String blockName(String block) {
        return "block.create." + block;
    }

    /**
     * Gets all the different colored toolboxes into an array of items
     */
    static {
        Iterator<BlockEntry<ToolboxBlock>> toolboxIterator = AllBlocks.TOOLBOXES.iterator();
        TOOLBOX_ITEMS = new Item[DyeColor.values().length];
        int i = 0;
        while(toolboxIterator.hasNext()) {
            TOOLBOX_ITEMS[i] = toolboxIterator.next().asItem();
            i++;
        }
    }
}
