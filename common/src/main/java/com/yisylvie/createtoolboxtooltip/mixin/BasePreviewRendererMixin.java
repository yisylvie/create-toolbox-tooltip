package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import java.util.Iterator;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.PreviewType;
import com.misterpemodder.shulkerboxtooltip.api.config.PreviewConfiguration;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer;
import com.misterpemodder.shulkerboxtooltip.impl.util.MergedItemStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("UnstableApiUsage")
@Mixin(BasePreviewRenderer.class)
public abstract class BasePreviewRendererMixin {

	@Shadow(remap = false)
	protected PreviewConfiguration config;

	@Shadow(remap = false)
	protected PreviewProvider provider;

	@Shadow(remap = false)
	protected PreviewContext previewContext;

	@Shadow(remap = false)
	protected PreviewType previewType;

	@Shadow(remap = false)
	protected List<MergedItemStack> items;

	@Final
	@Shadow(remap = false)
	private int slotWidth;

	/**
	 * Display items in toolbox tooltip that have a stack size of 0,
	 * and display item decorations for items with a stack size of 1.

	 *  If there are two items with a (compacted) stack >= 1000,
	 *  shifts the second tooltip over a tiny bit so they won't overlap
	 */
	@Inject(
		method = "drawItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V"
		),
		cancellable = true
	)
	private void createtoolboxtooltip$add0and1and1000Stacks(
			ItemStack stack, int x, int y, GuiGraphics context,
			Font textRenderer, int slot, boolean shortItemCount,
			CallbackInfo ci) {
		if (this.provider instanceof ToolboxPreviewProvider toolboxProvider) {
			if (this.previewType == PreviewType.FULL) {
				ItemStack realStack = toolboxProvider
						.getInventory(this.previewContext).get(slot);
				ItemStack compartmentStack = toolboxProvider
						.getCompartments(this.previewContext).get(slot);
				// the mergedItemStack does funky things, so we must regrab the (unmerged) stack
				if (realStack.isEmpty() && !compartmentStack.isEmpty()) {
					context.renderItem(compartmentStack, x, y);
					context.renderItemDecorations(
							textRenderer, compartmentStack, x, y, "0");
					ci.cancel();
				} else if (realStack.getCount() == 1) {
					context.renderItem(compartmentStack, x, y);
					context.renderItemDecorations(
							textRenderer, compartmentStack, x, y, "1");
					ci.cancel();
				}
			} else if (this.previewType == PreviewType.COMPACT) {
				if (this.items.size() > 1) {
					if (this.items.get(1).get() == stack) {
						if (this.items.get(0).get().getCount() >= 1000
								&& stack.getCount() >= 1000 && !shortItemCount) {
							context.renderItem(stack, x, y);
							context.renderItemDecorations(
									textRenderer, stack, x + (int)(this.slotWidth * .45),
									y, String.valueOf(stack.getCount()));
							ci.cancel();
						}
					}
				}
			}
		}
	}

	/**
	 * drawItems() iterates over the inventory tag,
	 * but we need to iterate over the compartments tag instead
	 * when rendering an empty inventory with nonempty compartments
	 *
	 * @param itemsIterator the original items iterator
	 * @return our new compartment iterator
	 */
	@ModifyVariable(
			method = "drawItems",
			at = @At("STORE"),
			ordinal = 0
	)
	private Iterator<MergedItemStack> createtoolboxtooltip$changeDrawItemsIterator(
			Iterator<MergedItemStack> itemsIterator) {
		if (this.provider instanceof ToolboxPreviewProvider
				&& !itemsIterator.hasNext()) {
			return createtoolboxtooltip$getCompartmentIterator(
					provider, previewContext, config);
		}
		return itemsIterator;
	}

	/**
	 * getStackAt() iterates over the inventory tag,
	 * but we need to iterate over the compartments tag instead
	 * so that we can show tooltips for empty stacks
	 *
	 * @param itemsIterator the original iterator
	 * @return our new compartment iterator
	 */
	@ModifyVariable(
			method = "getStackAt",
			at = @At("STORE"),
			ordinal = 0
	)
	private Iterator<MergedItemStack> createtoolboxtooltip$changeGetStackAtIterator(
			Iterator<MergedItemStack> itemsIterator) {
		if (this.provider instanceof ToolboxPreviewProvider) {
			return createtoolboxtooltip$getCompartmentIterator(
					provider, previewContext, config);
		}
		return itemsIterator;
	}

	/**
	 * Helper method to get the compartment iterator
	 * We first have to turn our original List<ItemStack> into a List<MergedItemStack>
	 */
	@Unique
	private static Iterator<MergedItemStack> createtoolboxtooltip$getCompartmentIterator(
			PreviewProvider provider, PreviewContext previewContext,
			PreviewConfiguration config) {
		ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider)provider;
		List<ItemStack> comp = toolboxProvider.getCompartments(previewContext);
		List<MergedItemStack> compMergedItemStacks = MergedItemStack.mergeInventory(
				comp,
				toolboxProvider.getInventoryMaxSize(previewContext),
				config.itemStackMergingStrategy());
		return compMergedItemStacks.iterator();
	}
}
