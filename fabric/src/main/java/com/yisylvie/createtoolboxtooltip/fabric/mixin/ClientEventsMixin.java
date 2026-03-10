package com.yisylvie.createtoolboxtooltip.fabric.mixin;

import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.simibubi.create.foundation.events.ClientEvents;

import com.yisylvie.createtoolboxtooltip.access.TooltipCategoryAccess;

import net.minecraft.network.chat.Component;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;

import java.util.List;

@Mixin(ClientEvents.class)
public abstract class ClientEventsMixin {

	/**
	 * disables the default Create toolbox tooltip if
	 * the enableCreateTooltips flag is set to false
	 */
	@Inject(
			method = "addToItemTooltip",
			at = @At(
					value = "HEAD"
			),
			cancellable = true
	)
	private static void createtoolboxtooltip$removeCreateDefaultTooltip(
			ItemStack stack, TooltipFlag iTooltipFlag,
			List<Component> itemTooltip, CallbackInfo ci) {
		if (((TooltipCategoryAccess) ShulkerBoxTooltip.config.tooltip)
				.createtoolboxtooltip$getHideCreateTooltips()
				&& stack.getItem() instanceof BlockItem block
				&& block.getBlock() instanceof ToolboxBlock) {
			ci.cancel();
		}
	}
}
