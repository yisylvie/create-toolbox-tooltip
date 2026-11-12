package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import java.lang.reflect.Field;

import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.renderer.PreviewRenderer;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.toolbox.RadialToolboxMenu;
import com.simibubi.create.content.equipment.toolbox.ToolboxBlockEntity;
import com.simibubi.create.content.equipment.toolbox.ToolboxInventory;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

@Mixin(RadialToolboxMenu.class)
public abstract class RadialToolboxMenuMixin extends Screen {

	@Unique
	private PreviewRenderer createtoolboxtooltip$renderer;

	@Unique
	private ToolboxPreviewProvider createtoolboxtooltip$provider;

	protected RadialToolboxMenuMixin(net.minecraft.network.chat.Component title) {
		super(title);
	}

	/**
	 * creates the PreviewRender and PreviewProvider for the
	 * currently hovered toolbox in the radial toolbox menu.
	 */
	@Inject(
			method = "renderWindow",
			at = @At(
					value = "INVOKE",
					target = "Lcom/simibubi/create/content/equipment/toolbox/ToolboxBlockEntity;getDisplayName()Lnet/minecraft/network/chat/Component;"
			)
	)
	private void createtoolboxtooltip$getHoveredToolbox(GuiGraphics graphics,
			int mouseX, int mouseY, float partialTicks, CallbackInfo ci,
			@Local(name = "toolboxBlockEntity") ToolboxBlockEntity toolboxBlockEntity) {
		try {
			// I couldn't figure out how to get the corresponding
			// ItemStack from the toolboxBlockEntity, so instead we
			// are grabbing the ToolboxInventory (which has all the
			// info we need) from toolboxBlockEntity and passing
			// it as a parameter to toolboxPreviewProvider.
			// We must use ✨reflection✨ to get toolboxBlockEntity.inventory
			// because it is package private
			Field inventory = ToolboxBlockEntity.class.getDeclaredField("inventory");

			this.createtoolboxtooltip$renderer = PreviewRenderer.getDefaultRendererInstance();
			this.createtoolboxtooltip$provider = new ToolboxPreviewProvider(
					(ToolboxInventory)inventory.get(toolboxBlockEntity));
			Level world = Minecraft.getInstance().level;
			PreviewContext context = PreviewContext.builder(
							AllBlocks.TOOLBOXES.get(toolboxBlockEntity.getColor()).asStack())
					.withRegistryLookup(world == null ? null : world.registryAccess())
					.build();
			this.createtoolboxtooltip$renderer.setPreview(
					context, this.createtoolboxtooltip$provider);
		} catch (NoSuchFieldException x) {
			x.printStackTrace();
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * adds a tooltip of toolbox content on the radial toolbox selection menu.
	 */
	@Inject(
			method = "renderWindow",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I"
			)
	)
	private void createtoolboxtooltip$drawToolboxTooltip(GuiGraphics graphics,
			int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (this.createtoolboxtooltip$renderer != null && this.createtoolboxtooltip$provider != null) {
			this.createtoolboxtooltip$renderer.draw(
					Math.round((float)(-this.createtoolboxtooltip$renderer.getWidth()) / 2.0F),
					-4 - this.createtoolboxtooltip$renderer.getHeight(),
					graphics, this.font, mouseX, mouseY);
		}
	}
}
