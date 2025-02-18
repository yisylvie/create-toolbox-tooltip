package com.yisylvie.createtoolboxtooltip;

// import java.util.ArrayList;
// import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
// import java.util.stream.Collectors;
import javax.annotation.Nonnull;

// import org.objectweb.asm.Type;

import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;
// import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;
import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorKey;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorRegistry;
// import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry;
// import com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer;

// import com.misterpemodder.shulkerboxtooltip.impl.provider.ShulkerBoxPreviewProvider;
// import com.misterpemodder.shulkerboxtooltip.impl.util.ShulkerBoxTooltipUtil;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
// import com.simibubi.create.foundation.block.DyedBlockList;
import com.tterrag.registrate.util.entry.BlockEntry;

import com.simibubi.create.AllBlocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
// import net.minecraft.nbt.CompoundTag;
// import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
// import net.minecraft.world.item.Items;
// import net.minecraft.world.level.block.Block;
// import net.minecraft.world.level.block.ShulkerBoxBlock;
// import net.minecraft.core.NonNullList;

// import net.minecraft.world.item.Items;
// import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class createToolboxTooltipPlugin implements ShulkerBoxTooltipApi {
    // BlockEntry<ToolboxBlock> baseShulker = AllBlocks.TOOLBOXES.get(DyeColor.BROWN);

    
    public static final Item[] TOOLBOX_ITEMS;
    
    // ItemStack baseShulkerStack = baseShulker.asStack();
    // Item baseShulkerItem = baseShulker.asItem();
    private static final ResourceLocation toolboxResourceLocation = new ResourceLocation("create", "toolboxes");
    
    @Override
    public void registerProviders(@Nonnull PreviewProviderRegistry registry) {
        
        // createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! toolbox description:" + baseShulkerItem.getDescription(), createToolboxTooltip.NAME, Create.VERSION);
		// createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! toolbox name:" + baseShulkerItem.getDescriptionId(), createToolboxTooltip.NAME, Create.VERSION);

		// createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz!" + brown_toolboxResourceLocation, createToolboxTooltip.NAME, Create.VERSION);
        registry.register(toolboxResourceLocation, new ToolboxPreviewProvider(), TOOLBOX_ITEMS);
    }
    
    // @Override
    // public void registerColors(ColorRegistry registry) {
    //     createToolboxTooltip.LOGGER.info(ColorKey.BROWN_SHULKER_BOX + "[{}] toolboxing deez nutz!" + AllBlocks.TOOLBOXES.get(DyeColor.BROWN), createToolboxTooltip.NAME, Create.VERSION);

    //     registry.category(brown_toolboxResourceLocation).register(ColorKey.BROWN_SHULKER_BOX, "brown_toolbox", "Brown Toolbox");
    // }
    @Override
    @Environment(EnvType.CLIENT)
    public void registerColors(@Nonnull ColorRegistry registry) {
        registry.category(toolboxResourceLocation)
            .register(ColorKey.WHITE_SHULKER_BOX, "white_toolbox", blockName("white_toolbox"))
            .register(ColorKey.ORANGE_SHULKER_BOX, "orange_toolbox", blockName("orange_toolbox"))
            .register(ColorKey.MAGENTA_SHULKER_BOX, "magenta_toolbox", blockName("magenta_toolbox"))
            .register(ColorKey.LIGHT_BLUE_SHULKER_BOX, "light_blue_toolbox", blockName("light_blue_toolbox"))
            .register(ColorKey.YELLOW_SHULKER_BOX, "yellow_toolbox", blockName("yellow_toolbox"))
            .register(ColorKey.LIME_SHULKER_BOX, "lime_toolbox", blockName("lime_toolbox"))
            .register(ColorKey.PINK_SHULKER_BOX, "pink_toolbox", blockName("pink_toolbox"))
            .register(ColorKey.GRAY_SHULKER_BOX, "gray_toolbox", blockName("gray_toolbox"))
            .register(ColorKey.LIGHT_GRAY_SHULKER_BOX, "light_gray_toolbox", blockName("light_gray_toolbox"))
            .register(ColorKey.CYAN_SHULKER_BOX, "cyan_toolbox", blockName("cyan_toolbox"))
            .register(ColorKey.PURPLE_SHULKER_BOX, "purple_toolbox", blockName("purple_toolbox"))
            .register(ColorKey.BLUE_SHULKER_BOX, "blue_toolbox", blockName("blue_toolbox"))
            .register(ColorKey.BROWN_SHULKER_BOX, "brown_toolbox", blockName("brown_toolbox"))
            .register(ColorKey.GREEN_SHULKER_BOX, "green_toolbox", blockName("green_toolbox"))
            .register(ColorKey.RED_SHULKER_BOX, "red_toolbox", blockName("red_toolbox"))
            .register(ColorKey.BLACK_SHULKER_BOX, "black_toolbox", blockName("black_toolbox"));
    }

    private static String blockName(String block) {
        return "block.create." + block;
    }

    public static void testing() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ItemStack handItemStack = player.getMainHandItem();
            PreviewContext preview = PreviewContext.of(handItemStack);
            ToolboxPreviewProvider hand_PreviewProvider = new ToolboxPreviewProvider();

            List<ItemStack> inventory = hand_PreviewProvider.getInventory(preview);
            List<ItemStack> compartments = hand_PreviewProvider.getCompartments(preview);

            // List<ItemStack> inventoryShulker = hand_PreviewProviderShulker.getInventory(preview);
            createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! shoulddisplay:" + hand_PreviewProvider.shouldDisplay(preview), createToolboxTooltip.NAME, Create.VERSION);
            createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! preview.stack.getTagEl:" + preview.stack().getTagElement("Inventory"), createToolboxTooltip.NAME, Create.VERSION);
            // createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! addtooltip:" + hand_PreviewProvider.addTooltip(preview), createToolboxTooltip.NAME, Create.VERSION);

            // createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! InventoryShulker:" + inventoryShulker, createToolboxTooltip.NAME, Create.VERSION);
            // for (ItemStack stack : inventoryShulker) {
            //     createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! item:" + stack.getItem() + handItemStack.getItem(), createToolboxTooltip.NAME, Create.VERSION);
            // }

            createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! Inventory:" + inventory, createToolboxTooltip.NAME, Create.VERSION);
            for (ItemStack stack : inventory) {
                createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! item:" + stack.getItem() + handItemStack.getItem(), createToolboxTooltip.NAME, Create.VERSION);
                // createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! max stack size:" + stack.getMaxStackSize() + handItemStack.getItem(), createToolboxTooltip.NAME, Create.VERSION);
            }

            createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! comp:" + compartments, createToolboxTooltip.NAME, Create.VERSION);
            for (ItemStack stack : compartments) {
                createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! compp item:" + stack.getItem() + handItemStack.getItem(), createToolboxTooltip.NAME, Create.VERSION);
                // createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! max stack size:" + stack.getMaxStackSize() + handItemStack.getItem(), createToolboxTooltip.NAME, Create.VERSION);
            }

            return InteractionResult.PASS;
        });
    }
    // <item:minecraft:pink_shulker_box>.withTag({BlockEntityTag: {id: "minecraft:shulker_box", Items: [{Slot: 0, id: "create:belt_connector", Count: 64}, {Slot: 1, id: "create:hand_crank", Count: 27}, {Slot: 2, id: "create:depot", Count: 64}, {Slot: 3, id: "create:chute", Count: 64}]}})
    // <item:create:brown_toolbox>.withTag({UniqueId: [-1971438192, 1141326940, -1225447979, -1616495346], Inventory: {Size: 32, Items: [{Slot: 0, Count: 64, id: "create:smart_chute"}, {Slot: 4, Count: 64, id: "create:speedometer"}, {Slot: 8, Count: 64, id: "create:mechanical_plough"}, {Slot: 9, Count: 64, id: "create:mechanical_plough"}, {Slot: 12, Count: 64, id: "create:portable_storage_interface"}, {Slot: 16, Count: 64, id: "create:deployer"}, {Slot: 20, Count: 64, id: "create:mechanical_saw"}, {Slot: 24, Count: 64, id: "create:creative_motor"}], Compartments: [{id: "create:smart_chute", Count: 1}, {id: "create:speedometer", Count: 1}, {id: "create:mechanical_plough", Count: 1}, {id: "create:portable_storage_interface", Count: 1}, {id: "create:deployer", Count: 1}, {id: "create:mechanical_saw", Count: 1}, {id: "create:creative_motor", Count: 1}, {id: "minecraft:air", Count: 0, tag: {Damage: 0}}]}})
    // public boolean isPreviewAvailable(PreviewContext context) {
    //     createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz!" + ShulkerBoxTooltipClient.isPreviewAvailable(context), createToolboxTooltip.NAME, Create.VERSION);

    //     return ShulkerBoxTooltipClient.isPreviewAvailable(context);
    // }

    // get all the different colored toolboxes into an array of items
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