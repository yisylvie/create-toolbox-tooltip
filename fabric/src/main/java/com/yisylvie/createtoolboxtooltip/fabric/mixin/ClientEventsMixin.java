package com.yisylvie.createtoolboxtooltip.fabric.mixin;

import com.yisylvie.createtoolboxtooltip.access.TooltipCategoryAccess;

import java.util.List;

import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;

import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.simibubi.create.foundation.events.ClientEvents;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.BlockItem;

@Mixin(ClientEvents.class)
public abstract class ClientEventsMixin {

	/**
	 * disables the default Create toolbox tooltip if
	 * the enableCreateTooltips flag is set to false
	 */
	@SuppressWarnings("UnstableApiUsage")
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
