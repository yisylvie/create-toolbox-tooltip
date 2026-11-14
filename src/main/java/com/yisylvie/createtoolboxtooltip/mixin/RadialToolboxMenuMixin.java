package com.yisylvie.createtoolboxtooltip.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yisylvie.createtoolboxtooltip.access.PreviewCategoryAccess;
import com.yisylvie.createtoolboxtooltip.access.ToolboxBlockEntityAccess;
import com.yisylvie.createtoolboxtooltip.access.VanillaPreviewRendererAccess;
import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltipClient;
import com.misterpemodder.shulkerboxtooltip.api.PreviewType;
import com.misterpemodder.shulkerboxtooltip.impl.config.ClientConfiguration;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.VanillaPreviewRenderer;
import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.renderer.PreviewRenderer;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.toolbox.RadialToolboxMenu;
import com.simibubi.create.content.equipment.toolbox.ToolboxBlockEntity;
import com.yisylvie.createtoolboxtooltip.createToolboxTooltip;

import dev.engine_room.flywheel.lib.transform.PoseTransformStack;
import dev.engine_room.flywheel.lib.transform.TransformStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

@SuppressWarnings("UnstableApiUsage")
@Mixin(RadialToolboxMenu.class)
public abstract class RadialToolboxMenuMixin extends Screen {
	@Unique
	private PreviewRenderer createtoolboxtooltip$renderer;

	@Unique
	private ToolboxPreviewProvider createtoolboxtooltip$provider;

	@Unique
	private PreviewContext createtoolboxtooltip$context;

	protected RadialToolboxMenuMixin(net.minecraft.network.chat.Component title) {
		super(title);
	}

	/**
	 * creates the PreviewRender, PreviewContext and PreviewProvider
	 * for the currently hovered toolbox in the radial toolbox menu.
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
		// I couldn't figure out how to get the corresponding
		// ItemStack from the toolboxBlockEntity, so instead we
		// are grabbing the ToolboxInventory (which has all the
		// info we need) from toolboxBlockEntity and passing
		// it as a parameter to toolboxPreviewProvider.
		ClientConfiguration config = ShulkerBoxTooltipClient.getConfig();
		PreviewType previewType = ((PreviewCategoryAccess) config.preview)
				.createtoolboxtooltip$getRadialToolboxMenuTooltip();
		if (previewType != PreviewType.NO_PREVIEW) {
            this.createtoolboxtooltip$renderer = PreviewRenderer.getDefaultRendererInstance();
                this.createtoolboxtooltip$provider = new ToolboxPreviewProvider(
                        ((ToolboxBlockEntityAccess)toolboxBlockEntity)
                                .createtoolboxtooltip$getToolboxInventory());
                Level world = Minecraft.getInstance().level;
				createtoolboxtooltip$context = PreviewContext.builder(
						AllBlocks.TOOLBOXES.get(toolboxBlockEntity.getColor()).asStack())
                        .withRegistryLookup(world == null ? null : world.registryAccess())
                        .build();
                this.createtoolboxtooltip$renderer.setPreview(
						createtoolboxtooltip$context, this.createtoolboxtooltip$provider);
                this.createtoolboxtooltip$renderer.setPreviewType(previewType);
		}
	}

	/**
	 * Moves the drawing origin location up a little
	 * to fit the tooltip on screen without overlap.
	 */
	@ModifyArg(
			method = "renderWindow",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
					ordinal = 4
			),
			index = 1
	)
	private float createtoolboxtooltip$changeContentHint(float y) {
		float hotbarHeight = 39;
		float radialMenuHeight = 53;
		float halfHeight = (float)this.height / 2.0F;
		float tooltipHeight = (float)createtoolboxtooltip$renderer.getHeight();
		if (createtoolboxtooltip$shouldDraw()) {
			createToolboxTooltip.LOGGER.info("gap width {}", halfHeight - (hotbarHeight + radialMenuHeight));
			createToolboxTooltip.LOGGER.info("tooltip width {}", tooltipHeight + 8);
			return halfHeight - (hotbarHeight + radialMenuHeight) > tooltipHeight + 8?
					(float)this.height - hotbarHeight - tooltipHeight - 4:
			hotbarHeight;
		}
		return y;
		// return createtoolboxtooltip$shouldDraw() ?
		// 		((float)(this.height) / 2.0F - (40.0F + 26)) : y;
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
		if (createtoolboxtooltip$shouldDraw()) {
			int width = this.createtoolboxtooltip$renderer.getWidth();
			// the vanillaRenderer's getWidth() doesn't get the true
			// width of the tooltip container but getColumnsWidth() does.
			if (createtoolboxtooltip$renderer instanceof VanillaPreviewRenderer vanillaRenderer) {
				width = ((VanillaPreviewRendererAccess) vanillaRenderer)
						.createtoolboxtooltip$getColumnsWidth();
			}
			this.createtoolboxtooltip$renderer.draw(
					Math.round((float) (-width) / 2.0F),
					4,
					graphics, this.font, mouseX, mouseY);
		}
	}

	/**
	 * helper method to check if we are drawing a tooltip
	 */
	@Unique
	private boolean createtoolboxtooltip$shouldDraw() {
		if (this.createtoolboxtooltip$renderer != null && this.createtoolboxtooltip$provider != null) {
			return this.createtoolboxtooltip$provider.shouldDisplay(this.createtoolboxtooltip$context);
		}
		return false;
	}
}
