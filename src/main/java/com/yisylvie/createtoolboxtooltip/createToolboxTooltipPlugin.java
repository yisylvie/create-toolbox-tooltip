package com.yisylvie.createtoolboxtooltip;

import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltipClient;
import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
// import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;
import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorKey;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorRegistry;
// import com.misterpemodder.shulkerboxtooltip.api.color.ColorRegistry;
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider;
// import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry;
import com.misterpemodder.shulkerboxtooltip.impl.config.Configuration;
import com.misterpemodder.shulkerboxtooltip.impl.config.ConfigurationHandler;
import com.misterpemodder.shulkerboxtooltip.impl.network.ServerNetworking;
import com.misterpemodder.shulkerboxtooltip.impl.util.ShulkerBoxTooltipUtil;
import com.simibubi.create.AllBlocks;
// import com.simibubi.create.content.equipment.toolbox.ToolBoxInstance;
// import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.simibubi.create.Create;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
// import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
// import io.github.fabricators_of_create.porting_lib.tags.Tags;
// import com.misterpemodder.shulkerboxtooltip.impl.network.ServerNetworking;
// import net.fabricmc.api.EnvType;
// import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
// import net.minecraft.server.level.ServerPlayer;
// import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

// import org.jetbrains.annotations.ApiStatus;

// import javax.annotation.Nonnull;
// import javax.annotation.Nullable;

public class createToolboxTooltipPlugin implements ShulkerBoxTooltipApi {
    Item baseShulkerStack = AllBlocks.TOOLBOXES.get(DyeColor.BROWN).asItem();
    ResourceLocation brown_toolbox = new ResourceLocation("create", "brown_toolbox");
    
    @Override
    public void registerProviders(PreviewProviderRegistry registry) {
        createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz!" + baseShulkerStack, createToolboxTooltip.NAME, Create.VERSION);

		createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz!" + brown_toolbox, createToolboxTooltip.NAME, Create.VERSION);
        registry.register(brown_toolbox,
            new BlockEntityPreviewProvider(8, true), baseShulkerStack);

        registry.register(brown_toolbox,
            new BlockEntityPreviewProvider(8, true), baseShulkerStack);

    }
    
    @Override
    public void registerColors(ColorRegistry registry) {
        createToolboxTooltip.LOGGER.info(ColorKey.BROWN_SHULKER_BOX + "[{}] toolboxing deez nutz!" + AllBlocks.TOOLBOXES.get(DyeColor.BROWN), createToolboxTooltip.NAME, Create.VERSION);

        registry.category(brown_toolbox).register(ColorKey.BROWN_SHULKER_BOX, "brown_toolbox", "Brown Toolbox");
    }
    // public boolean isPreviewAvailable(PreviewContext context) {
    //     createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz!" + ShulkerBoxTooltipClient.isPreviewAvailable(context), createToolboxTooltip.NAME, Create.VERSION);

    //     return ShulkerBoxTooltipClient.isPreviewAvailable(context);
    // }
}