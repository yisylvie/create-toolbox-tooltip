package com.yisylvie.createtoolboxtooltip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

// import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltipClient;
import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorKey;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorRegistry;
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry;
import com.misterpemodder.shulkerboxtooltip.impl.provider.ShulkerBoxPreviewProvider;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.yisylvie.createtoolboxtooltip.api.toolboxPreviewProvider;
import com.simibubi.create.AllBlocks;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

// import net.minecraft.world.item.Items;

public class createToolboxTooltipPlugin implements ShulkerBoxTooltipApi {
    // BlockEntry<ToolboxBlock> baseShulker = AllBlocks.TOOLBOXES.get(DyeColor.BROWN);

    
    public static final Item[] TOOLBOX_ITEMS;
    
    // ItemStack baseShulkerStack = baseShulker.asStack();
    // Item baseShulkerItem = baseShulker.asItem();
    private static final ResourceLocation toolboxResourceLocation = new ResourceLocation("create", "toolboxes");
    
    @Override
    public void registerProviders(PreviewProviderRegistry registry) {
        
        // createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! toolbox description:" + baseShulkerItem.getDescription(), createToolboxTooltip.NAME, Create.VERSION);
		// createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! toolbox name:" + baseShulkerItem.getDescriptionId(), createToolboxTooltip.NAME, Create.VERSION);

		// createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz!" + brown_toolboxResourceLocation, createToolboxTooltip.NAME, Create.VERSION);
        registry.register(toolboxResourceLocation, new toolboxPreviewProvider(), TOOLBOX_ITEMS);
    }

    // @Override
    // public boolean showTooltipHints(PreviewContext context) {
    //     return context.stack().getTagElement("BlockEntityTag") != null;
    // }
    
    // @Override
    // public void registerColors(ColorRegistry registry) {
    //     createToolboxTooltip.LOGGER.info(ColorKey.BROWN_SHULKER_BOX + "[{}] toolboxing deez nutz!" + AllBlocks.TOOLBOXES.get(DyeColor.BROWN), createToolboxTooltip.NAME, Create.VERSION);

    //     registry.category(brown_toolboxResourceLocation).register(ColorKey.BROWN_SHULKER_BOX, "brown_toolbox", "Brown Toolbox");
    // }

    public static void testing() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ItemStack handItemStack = player.getMainHandItem();
            PreviewContext preview = PreviewContext.of(handItemStack);
            toolboxPreviewProvider hand_PreviewProvider = new toolboxPreviewProvider();

            List<ItemStack> inventory = hand_PreviewProvider.getInventory(preview);

            // List<ItemStack> inv = NonNullList.withSize(8, ItemStack.EMPTY);
            CompoundTag InventoryTag = preview.stack().getTagElement("Inventory");
            // if (InventoryTag != null && InventoryTag.contains("Items", InventoryTag.getTagType("Items"))) {
            ListTag itemList = InventoryTag.getList("Items", 9);
                // if (itemList != null) {
            
            // CompoundTag InventoryTag = preview.stack().getTagElement("Inventory");
            // CompoundTag compound = preview.stack().getTag();
            // byte tagType = compound.getTagType("Inventory");
      
            // CompoundTag blockEntityTag = handItemStack.getTagElement("BlockEntityTag");
            createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! inventory tag type" + InventoryTag, createToolboxTooltip.NAME, Create.VERSION);

            // createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz!" + handItem.getItem(), createToolboxTooltip.NAME, Create.VERSION);
            for (ItemStack stack : inventory) {
                createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! item:" + stack.getItem() + handItemStack.getItem(), createToolboxTooltip.NAME, Create.VERSION);
            }

            createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! itemlist itemlist:" + itemList + handItemStack.getItem(), createToolboxTooltip.NAME, Create.VERSION);
            createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! itemlist itemlist:" + itemList.size() + handItemStack.getItem(), createToolboxTooltip.NAME, Create.VERSION);
            for(int i = 0; i < itemList.size(); ++i) {
                CompoundTag itemTag = itemList.getCompound(i);
                ItemStack s = ItemStack.of(itemTag);
                createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! itemlist item:" + s.getItem() + handItemStack.getItem(), createToolboxTooltip.NAME, Create.VERSION);
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