package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.createToolboxTooltip;
import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.Shadow;
import org.apache.commons.compress.harmony.pack200.NewAttributeBands.Call;
import org.spongepowered.asm.mixin.Final;
// import org.spongepowered.asm.mixin.Unique;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import org.objectweb.asm.Type;

import net.minecraft.world.entity.Entity;

@Mixin(BasePreviewRenderer.class)
public abstract class BasePreviewRendererMixin {

	@Shadow(remap = false)
	protected PreviewProvider provider;

	@Shadow(remap = false)
	protected PreviewContext previewContext;

	@ModifyVariable(
		method = "drawItem", 
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V"
			// "HEAD"
			)
		// cancellable=true,
		// locals = LocalCapture.CAPTURE_FAILHARD
	)
	private ItemStack createtoolboxtooltip$drawZeroItem(ItemStack stack, int x, int y, GuiGraphics context,
			Font textRenderer, int slot, boolean shortItemCount, CallbackInfo ci, @Local LocalRef<String> countLabel) {
		if(provider instanceof ToolboxPreviewProvider && stack.getCount() == 0) {
			ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider)provider;
			countLabel.set("0");
			createToolboxTooltip.LOGGER.info("{} deez I'm a toolbox!!" + stack, createToolboxTooltip.NAME);
			// ci.cancel();
			return toolboxProvider.getCompartments(previewContext).get(slot);
		} 
		return stack;
	}
}

// Expected (Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;IZLorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V 
// but found (Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;ZILorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V