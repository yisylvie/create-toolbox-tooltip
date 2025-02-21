package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.createToolboxTooltip;
import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import java.util.Iterator;
import java.util.List;

import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.PreviewType;
import com.misterpemodder.shulkerboxtooltip.api.config.PreviewConfiguration;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.VanillaPreviewRenderer;
import com.misterpemodder.shulkerboxtooltip.impl.util.MergedItemStack;
import com.simibubi.create.Create;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// import org.objectweb.asm.Type;

// import net.minecraft.world.entity.Entity;

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

	// Display items in toolbox tooltip that have a stack size of 0,
	// and display item decorations for items with a stack size of 1.
	@Inject(
		method = "drawItem", 
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V"
		),
		cancellable = true
	)
	private void createtoolboxtooltip$add0and1Stacks(ItemStack stack, int x, 
			int y, GuiGraphics context, Font textRenderer, 
			int slot, boolean shortItemCount, 
			CallbackInfo ci) {
				
		if(this.provider instanceof ToolboxPreviewProvider && this.previewType == PreviewType.FULL) {
			ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider)this.provider;

			// the mergedItemStack does funky things, so we must regrab the (unmerged) stack
			ItemStack realStack = toolboxProvider.getInventory(this.previewContext).get(slot);
			ItemStack compartmentStack = toolboxProvider.getCompartments(this.previewContext).get(slot);
			if(realStack.isEmpty() && !compartmentStack.isEmpty()) {
				context.renderItem(compartmentStack, x, y);
				context.renderItemDecorations(textRenderer, compartmentStack, x, y, "0");
				ci.cancel();
			} else if(realStack.getCount() == 1) {
				context.renderItem(compartmentStack, x, y);
				context.renderItemDecorations(textRenderer, compartmentStack, x, y, "1");
				ci.cancel();
			}
		}
	}

	// drawItems() iterates over the inventory tag,
	// but we need to iterate over the compartments tag instead 
	// when rendering an empty inventory with nonempty compartments
	@ModifyVariable(
		method = "drawItems", 
		at = @At("STORE"), 
		ordinal = 0
	)
	private Iterator<MergedItemStack> createtoolboxtooltip$changeDrawItemsIterator(
			Iterator<MergedItemStack> itemsIterator) {
		if(this.provider instanceof ToolboxPreviewProvider && !itemsIterator.hasNext()) {
			return createtoolboxtooltip$getCompartmentIterator(provider, previewContext, config);
		} 
		return itemsIterator;
	}

	// getStackAt() iterates over the inventory tag,
	// but we need to iterate over the compartments tag instead
	// so that we can show tooltips for empty stacks
	@ModifyVariable(
		method = "getStackAt", 
		at = @At("STORE"), 
		ordinal = 0
	)
	private Iterator<MergedItemStack> createtoolboxtooltip$changeGetStackAtIterator(
			Iterator<MergedItemStack> itemsIterator) {
		if (this.provider instanceof ToolboxPreviewProvider) {
			return createtoolboxtooltip$getCompartmentIterator(provider, previewContext, config);
		}
		return itemsIterator;
	}

	private static Iterator<MergedItemStack> createtoolboxtooltip$getCompartmentIterator(
			PreviewProvider provider, PreviewContext previewContext, PreviewConfiguration config) {
		ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider)provider;
		List<ItemStack> comp = toolboxProvider.getCompartments(previewContext);
		List<MergedItemStack> compMergedItemStacks = MergedItemStack.mergeInventory(
				comp,
				toolboxProvider.getInventoryMaxSize(previewContext),
				config.itemStackMergingStrategy());
		return compMergedItemStacks.iterator();
	}

	// @ModifyArgs(
	// 	method = "drawItem", 
	// 	at = @At(
	// 		value = "INVOKE",
	// 		target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"
	// 		// "HEAD"
	// 		)
	// 	// cancellable=true,
	// 	// locals = LocalCapture.CAPTURE_FAILHARD
	// )
	// private void createtoolboxtooltip$changeStackDecoration(Args args, 
	// 		@Local(argsOnly = true, ordinal = 2) int slotRef) {
	// 	if(provider instanceof ToolboxPreviewProvider 
	// 						&& previewType == PreviewType.FULL) {
	// 		ItemStack stack = args.get(1);
	// 		if(stack.isEmpty()) {
	// 			ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider)provider;
	// 			args.set(1, toolboxProvider.getCompartments(previewContext).get(slotRef));
	// 			args.set(4, "0");
	// 		} else if(stack.getCount() == 1) {
	// 			args.set(4, "1");
	// 		}
	// 	} 
	// }

	// @ModifyVariable(
	// 	method = "drawItem", 
	// 	at = @At(
	// 		value = "INVOKE",
	// 		target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V"
	// 		// "HEAD"
	// 		)
	// 		// , index = 0
	// 	// cancellable=true,
	// 	// locals = LocalCapture.CAPTURE_FAILHARD
	// )
	// private ItemStack createtoolboxtooltip$changeStack(ItemStack stack, 
	// 		@Local(argsOnly = true, ordinal = 2) int slotRef,
	// 		@Local LocalRef<String> countLabelRef) {
	// 	ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider)provider;
	// 	ItemStack realStack = toolboxProvider.getInventory(previewContext).get(slotRef);

	// 	if(provider instanceof ToolboxPreviewProvider && previewType == PreviewType.FULL
	// 			&& (realStack.getCount() == 1 || realStack.isEmpty())) {
	// 		ItemStack compartmentStack = toolboxProvider.getCompartments(previewContext).get(slotRef);
	// 		if(realStack.isEmpty() && !compartmentStack.isEmpty()) {
	// 			createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! stack:" + realStack + slotRef,
	// 					createToolboxTooltip.NAME, Create.VERSION);
	// 			countLabelRef.set("0");
	// 			return compartmentStack;
	// 		} else if(realStack.getCount() == 1) {
	// 			countLabelRef.set("1");
	// 			return compartmentStack;
	// 		} 
	// 	}

	// 	return stack;
	// }

	// @ModifyArgs(
	// 	method = "drawItems", 
	// 	at = @At(
	// 		value = "INVOKE",
	// 		target = "Lcom/misterpemodder/shulkerboxtooltip/impl/renderer/BasePreviewRenderer;drawItem(Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;IZ)V"
	// 		// "HEAD"
	// 		)
	// 		// , index = 0
	// 	// cancellable=true,
	// 	// locals = LocalCapture.CAPTURE_FAILHARD
	// )
}