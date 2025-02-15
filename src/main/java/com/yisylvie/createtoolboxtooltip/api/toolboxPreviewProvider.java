package com.yisylvie.createtoolboxtooltip.api;

import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorKey;
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider;

import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.simibubi.create.content.equipment.toolbox.ToolboxInventory;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
// import net.minecraft.client.gui.GuiGraphics;

// Create uses the "Inventory" tag instead of the "BlockEntity" tag to store data about toolbox contents,
// so we must replace every instance of the "BlockEntity" tag with the "Inventory" tag.
public class ToolboxPreviewProvider extends BlockEntityPreviewProvider {
   public ToolboxPreviewProvider() {
      super(8, false);
   }

   public boolean shouldDisplay(@Nonnull PreviewContext context) {
      CompoundTag InventoryTag = context.stack().getTagElement("Inventory");
      if (InventoryTag != null) {
         return getItemCount(this.getInventory(context)) > 0;
      } else {
         return false;
      }
   }

   // If the toolbox is stackable, the Inventory tag doesn't exist (and thus we will not show tooltips)
   // It seems as though a toolbox loses its stackability when placed in the world
   public boolean showTooltipHints(@Nonnull PreviewContext context) {
      return context.stack().getTagElement("Inventory") != null;
   }

   // copied almost exactly from ShulkerBoxPreviewProvider
   @Environment(EnvType.CLIENT)
   public ColorKey getWindowColorKey(@Nonnull PreviewContext context) {
      DyeColor dye = ((ToolboxBlock) Block.byItem(context.stack().getItem())).getColor();
      if (dye == null)
         return ColorKey.BROWN_SHULKER_BOX;
      return switch (dye) {
         case ORANGE -> ColorKey.ORANGE_SHULKER_BOX;
         case MAGENTA -> ColorKey.MAGENTA_SHULKER_BOX;
         case LIGHT_BLUE -> ColorKey.LIGHT_BLUE_SHULKER_BOX;
         case YELLOW -> ColorKey.YELLOW_SHULKER_BOX;
         case LIME -> ColorKey.LIME_SHULKER_BOX;
         case PINK -> ColorKey.PINK_SHULKER_BOX;
         case GRAY -> ColorKey.GRAY_SHULKER_BOX;
         case LIGHT_GRAY -> ColorKey.LIGHT_GRAY_SHULKER_BOX;
         case CYAN -> ColorKey.CYAN_SHULKER_BOX;
         case PURPLE -> ColorKey.PURPLE_SHULKER_BOX;
         case BLUE -> ColorKey.BLUE_SHULKER_BOX;
         case BROWN -> ColorKey.BROWN_SHULKER_BOX;
         case GREEN -> ColorKey.GREEN_SHULKER_BOX;
         case RED -> ColorKey.RED_SHULKER_BOX;
         case BLACK -> ColorKey.BLACK_SHULKER_BOX;
         default -> ColorKey.WHITE_SHULKER_BOX;
      };
   }

   public List<ItemStack> getInventory(@Nonnull PreviewContext context) {
      int invMaxSize = this.getInventoryMaxSize(context);
      List<ItemStack> inv = NonNullList.withSize(invMaxSize, ItemStack.EMPTY);
      CompoundTag InventoryTag = context.stack().getTagElement("Inventory");

      if (InventoryTag != null && InventoryTag.contains("Items", 9)) {
         // I have no idea why the getList() method is not working, but this seems to be
         ListTag itemList = (ListTag)InventoryTag.get("Items");

         if (itemList != null) {
            for (int compartment = 0; compartment < this.getInventoryMaxSize(context); compartment++) {
               int baseIndex = compartment * ToolboxInventory.STACKS_PER_COMPARTMENT;
               ItemStack s = ItemStack.EMPTY;
               int count = 0;
               for(int i = 0; i < itemList.size(); ++i) {
                  CompoundTag itemTag = itemList.getCompound(i);
                  if (itemTag.contains("Slot", 99)) {
                     if (itemTag.getInt("Slot") == baseIndex) {
                        s = ItemStack.of(itemTag);
                        count = s.getCount();
                     } else if(itemTag.getInt("Slot") > baseIndex && itemTag.getInt("Slot") < baseIndex + 4) {
                        count += ItemStack.of(itemTag).getCount();
                     }
                  }
               }

               if (!s.isEmpty()) {
                  s.setCount(count);
                  inv.set(compartment, s);
               }
            }
         }
      }
      return inv;
   }

   private static int getItemCount(@Nullable List<ItemStack> items) {
      int itemCount = 0;
      if (items != null) {
         Iterator<ItemStack> itemIter = items.iterator();

         while(itemIter.hasNext()) {
            ItemStack stack = (ItemStack)itemIter.next();
            if (stack.getItem() != Items.AIR) {
               ++itemCount;
            }
         }
      }

      return itemCount;
   }
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