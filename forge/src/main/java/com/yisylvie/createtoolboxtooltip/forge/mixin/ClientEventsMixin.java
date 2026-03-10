package com.yisylvie.createtoolboxtooltip.forge.mixin;

import com.yisylvie.createtoolboxtooltip.access.TooltipCategoryAccess;

import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;

import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.simibubi.create.foundation.events.ClientEvents;

import net.minecraft.world.item.BlockItem;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
			cancellable = true,
			remap = false
	)
	private static void createtoolboxtooltip$removeCreateDefaultTooltip(
			ItemTooltipEvent event, CallbackInfo ci) {
		if (((TooltipCategoryAccess) ShulkerBoxTooltip.config.tooltip)
				.createtoolboxtooltip$getHideCreateTooltips()
				&& event.getItemStack().getItem() instanceof BlockItem block
				&& block.getBlock() instanceof ToolboxBlock) {
			ci.cancel();
		}
	}
}
