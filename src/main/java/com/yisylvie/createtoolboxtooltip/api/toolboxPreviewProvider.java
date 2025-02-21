package com.yisylvie.createtoolboxtooltip.api;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.PreviewType;
import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorKey;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorRegistry;
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider;
import com.misterpemodder.shulkerboxtooltip.impl.color.ColorRegistryImpl;
import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.simibubi.create.content.equipment.toolbox.ToolboxInventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

// Create uses the "Inventory" tag instead of the "BlockEntity" tag to store data about toolbox contents,
// so we must replace every instance of the "BlockEntity" tag with the "Inventory" tag.
public class ToolboxPreviewProvider extends BlockEntityPreviewProvider {

   // Idk what a loot table does so we're setting that to false
   public ToolboxPreviewProvider() {
      super(8, false);
   }

   // should only display in compact mode if there are actual items in the inventory,
   // but if there are 0 stack compartments, we can still display in full preview mode
   public boolean shouldDisplay(@Nonnull PreviewContext context) {
      CompoundTag InventoryTag = context.stack().getTagElement("Inventory");
      if (InventoryTag != null) {
         // PreviewType previewType = ShulkerBoxTooltipApi.getCurrentPreviewType(this.isFullPreviewAvailable(context));
         // if(previewType == PreviewType.COMPACT) {
            // return getItemCount(this.getInventory(context)) > 0;
         // }
         return getItemCount(this.getCompartments(context)) > 0;
      } 
      return false;
   }

   // If the toolbox is stackable, the Inventory tag doesn't exist (and thus we will not show tooltips)
   // It seems as though a toolbox loses its stackability when placed in the world
   public boolean showTooltipHints(@Nonnull PreviewContext context) {
      return context.stack().getTagElement("Inventory") != null;
   }

   @Environment(EnvType.CLIENT)
   public ColorKey getWindowColorKey(@Nonnull PreviewContext context) {
      ColorRegistryImpl colorRegistry = ColorRegistryImpl.INSTANCE;
      ColorRegistry.Category category = colorRegistry.category(
                     new ResourceLocation("create", "toolboxes"));

      DyeColor dye = ((ToolboxBlock) Block.byItem(context.stack().getItem())).getColor();
      if (dye == null)
         return category.key("brown_toolbox");
      return switch (dye) {
         case ORANGE -> category.key("orange_toolbox");
         case MAGENTA -> category.key("magenta_toolbox");
         case LIGHT_BLUE -> category.key("light_blue_toolbox");
         case YELLOW -> category.key("yellow_toolbox");
         case LIME -> category.key("lime_toolbox");
         case PINK -> category.key("pink_toolbox");
         case GRAY -> category.key("gray_toolbox");
         case LIGHT_GRAY -> category.key("light_gray_toolbox");
         case CYAN -> category.key("cyan_toolbox");
         case PURPLE -> category.key("purple_toolbox");
         case BLUE -> category.key("blue_toolbox");
         case BROWN -> category.key("brown_toolbox");
         case GREEN -> category.key("green_toolbox");
         case RED -> category.key("red_toolbox");
         case BLACK -> category.key("black_toolbox");
         default -> category.key("white_toolbox");
      };
   }

   // Toolboxes are stored with 32 stacks in the Items tag, 
   // since each slot in a toolbox can fit 4 ordinary sized stacks.
   // We must consolidate these 32 stacks back down into 8, 
   // so that they can be displayed properly
   public List<ItemStack> getInventory(@Nonnull PreviewContext context) {
      int invMaxSize = this.getInventoryMaxSize(context);
      List<ItemStack> inv = NonNullList.withSize(invMaxSize, ItemStack.EMPTY);
      CompoundTag InventoryTag = context.stack().getTagElement("Inventory");

      if (InventoryTag != null && InventoryTag.contains("Items", 9)) {
         // I have no idea why the getList() method is not working, but this seems to be
         ListTag itemList = (ListTag)InventoryTag.get("Items");

         if (itemList != null) {
            for (int compartment = 0; compartment < invMaxSize; compartment++) {
               int baseIndex = compartment * ToolboxInventory.STACKS_PER_COMPARTMENT;
               ItemStack s = ItemStack.EMPTY;
               int count = 0;
               for(int i = 0; i < itemList.size(); ++i) {
                  CompoundTag itemTag = itemList.getCompound(i);
                  if (itemTag.contains("Slot", 99)) {
                     if (itemTag.getInt("Slot") == baseIndex) {
                        s = ItemStack.of(itemTag);
                        count = s.getCount();
                     } else if(itemTag.getInt("Slot") > baseIndex 
                                    && itemTag.getInt("Slot") < baseIndex + 4) {
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

   // Toolboxes have a special compartments tag to store what items are in each 
   // of their 8 compartments. We must store this so that we can display any empty stacks
   public List<ItemStack> getCompartments(PreviewContext context) {
      int invMaxSize = this.getInventoryMaxSize(context);
      List<ItemStack> comp = NonNullList.withSize(invMaxSize, ItemStack.EMPTY);
      CompoundTag InventoryTag = context.stack().getTagElement("Inventory");

      if (InventoryTag != null && InventoryTag.contains("Compartments", 9)) {
         ListTag compartmentsList = InventoryTag.getList("Compartments", 10);
         if (compartmentsList != null) {
            for(int i = 0; i < compartmentsList.size(); ++i) {
               CompoundTag compartmentsTag = compartmentsList.getCompound(i);
               ItemStack s = ItemStack.of(compartmentsTag);
               comp.set(i, s);
            }
         }
      }
      return comp;
   }

   public static int getItemCount(@Nullable List<ItemStack> items) {
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

   public Boolean isInventoryEmpty(PreviewContext context) {
      return getItemCount(this.getInventory(context)) == 0 && getItemCount(this.getCompartments(context)) != 0;
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