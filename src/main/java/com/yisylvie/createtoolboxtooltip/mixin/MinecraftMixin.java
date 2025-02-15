package com.yisylvie.createtoolboxtooltip.mixin;

import com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer;
import com.yisylvie.createtoolboxtooltip.createToolboxTooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

// import net.minecraft.client.Minecraft;

import net.minecraft.client.main.GameConfig;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BasePreviewRenderer.class)
public class MinecraftMixin {
	@Inject(method = "drawItem(Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Z)V", at = @At("TAIL"))
	private void drawItem(ItemStack stack, int x, int y, GuiGraphics graphics, Font font, int mystery, boolean shortItemCount, CallbackInfo ci) {
		createToolboxTooltip.LOGGER.info("Hello?????? from {}", createToolboxTooltip.NAME);
	}
}
// Expected (Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;IZLorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V 
// but found (Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;ZILorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V