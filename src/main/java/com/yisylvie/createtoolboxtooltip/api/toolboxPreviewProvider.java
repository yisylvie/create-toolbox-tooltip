package com.yisylvie.createtoolboxtooltip.api;

import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;
import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.PreviewType;
import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorKey;
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider;
import com.misterpemodder.shulkerboxtooltip.impl.config.Configuration.LootTableInfoType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class toolboxPreviewProvider extends BlockEntityPreviewProvider {
   public toolboxPreviewProvider() {
      super(8, true);
   }

   public boolean showTooltipHints(PreviewContext context) {
      return true;
   }

   public List<ItemStack> getInventory(PreviewContext context) {
      int invMaxSize = this.getInventoryMaxSize(context);
      List<ItemStack> inv = NonNullList.withSize(invMaxSize, ItemStack.EMPTY);
      CompoundTag InventoryTag = context.stack().getTagElement("Inventory");
      if (InventoryTag != null && InventoryTag.contains("Items", InventoryTag.getTagType("Items"))) {
         ListTag itemList = InventoryTag.getList("Items", InventoryTag.getTagType("Items"));
         if (itemList != null) {
            for(int i = 0; i < itemList.size(); ++i) {
               CompoundTag itemTag = itemList.getCompound(i);
               ItemStack s = ItemStack.of(itemTag);
               if (itemTag.contains("Slot", 99)) {
                  int slot = itemTag.getInt("Slot");
                  if (slot >= 0 && slot < invMaxSize) {
                     inv.set(slot, s);
                  }
               }
            }
         }
      }

      return inv;
   }

   // public List<Component> addTooltip(PreviewContext context) {
   //    // ItemStack stack = context.stack();
   //    // CompoundTag compound = stack.getTag();
   //    Style style = Style.EMPTY.withColor(ChatFormatting.GRAY);
   //    // if (this.canUseLootTables && compound != null && compound.contains("Inventory", compound.getTagType("Inventory"))) {
   //    //    CompoundTag InventoryTag = compound.getCompound("Inventory");
   //    //    if (InventoryTag != null) {
   //    //       return switch (ShulkerBoxTooltip.config.tooltip.lootTableInfoType) {
   //    //          case HIDE -> Collections.emptyList();
   //    //          case SIMPLE -> Collections.singletonList(Component.translatable("shulkerboxtooltip.hint.lootTable").setStyle(style));
   //    //          default -> Arrays.asList(Component.translatable("shulkerboxtooltip.hint.lootTable.advanced").append(Component.literal(": ")), Component.literal(" " + InventoryTag.getString("LootTable")).setStyle(style));
   //    //       };
   //    //    }
   //    // }

   //    return ShulkerBoxTooltipApi.getCurrentPreviewType(this.isFullPreviewAvailable(context)) == PreviewType.FULL ? Collections.emptyList() : getItemListTooltip(new ArrayList<>(), this.getInventory(context), style);
   // }
}

// <item:create:brown_toolbox>.withTag({
//    UniqueId: 
//       [-1503814404, 1573406524, -1544085369, -1789837592], 
//       Inventory: 
//          {Size: 32, 
//             Items: 
//                [{
//                   Slot: 0, Count: 64, id: "minecraft:oak_log"}, 
//                   {Slot: 1, Count: 64, id: "minecraft:oak_log"}, 
//                   {Slot: 2, Count: 64, id: "minecraft:oak_log"}, 
//                   {Slot: 3, Count: 64, id: "minecraft:oak_log"},
//                   {Slot: 4, Count: 64, id: "minecraft:stripped_oak_log"}, 
//                   {Slot: 5, Count: 64, id: "minecraft:stripped_oak_log"}, 
//                   {Slot: 6, Count: 64, id: "minecraft:stripped_oak_log"}, 
//                   {Slot: 8, Count: 64, id: "minecraft:oak_wood"}, 
//                   {Slot: 9, Count: 64, id: "minecraft:oak_wood"}, 
//                   {Slot: 10, Count: 64, id: "minecraft:oak_wood"}, 
//                   {Slot: 11, Count: 64, id: "minecraft:oak_wood"}, 
//                   {Slot: 12, Count: 64, id: "minecraft:stripped_oak_wood"}, 
//                   {Slot: 13, Count: 64, id: "minecraft:stripped_oak_wood"}, 
//                   {Slot: 14, Count: 64, id: "minecraft:stripped_oak_wood"}, 
//                   {Slot: 16, Count: 64, id: "minecraft:acacia_wood"}, 
//                   {Slot: 20, Count: 64, id: "minecraft:stripped_acacia_log"}, 
//                   {Slot: 21, Count: 64, id: "minecraft:stripped_acacia_log"}, 
//                   {Slot: 24, Count: 64, id: "minecraft:stripped_acacia_wood"}, 
//                   {Slot: 25, Count: 64, id: "minecraft:stripped_acacia_wood"}], 
//                   Compartments: [{id: "minecraft:oak_log", Count: 1}, 
//                   {id: "minecraft:stripped_oak_log", Count: 1}, 
//                   {id: "minecraft:oak_wood", Count: 1}, 
//                   {id: "minecraft:stripped_oak_wood", Count: 1}, 
//                   {id: "minecraft:acacia_wood", Count: 1}, 
//                   {id: "minecraft:stripped_acacia_log", Count: 1}, 
//                   {id: "minecraft:stripped_acacia_wood", Count: 1}, 
//                   {id: "minecraft:air", Count: 0, tag: {Damage: 0}}]}})

// <item:create:orange_toolbox>.withTag({UniqueId: [-739423658, 1009405281, -1762863542, 1652184297], Inventory: {Size: 32, Items: [], Compartments: [{id: "minecraft:air", Count: 0, tag: {Damage: 0}}, {id: "minecraft:air", Count: 0, tag: {Damage: 0}}, {id: "minecraft:air", Count: 0, tag: {Damage: 0}}, {id: "minecraft:air", Count: 0, tag: {Damage: 0}}, {id: "minecraft:air", Count: 0, tag: {Damage: 0}}, {id: "minecraft:air", Count: 0, tag: {Damage: 0}}, {id: "minecraft:air", Count: 0, tag: {Damage: 0}}, {id: "minecraft:air", Count: 0, tag: {Damage: 0}}]}})