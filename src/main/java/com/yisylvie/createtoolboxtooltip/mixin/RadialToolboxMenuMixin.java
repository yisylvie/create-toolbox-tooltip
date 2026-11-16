package com.yisylvie.createtoolboxtooltip.mixin;

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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

@SuppressWarnings("UnstableApiUsage")
@Mixin(RadialToolboxMenu.class)
public abstract class RadialToolboxMenuMixin extends Screen {
	@Shadow
	private ToolboxBlockEntity selectedBox;

	protected RadialToolboxMenuMixin(net.minecraft.network.chat.Component title) {
		super(title);
	}

	/**
	 * Draws the toolbox preview for the currently
	 * hovered toolbox in the radial toolbox menu.
	 * this.selectedBox is null (because we haven't selected
	 * a box yet), so me must get our ToolboxBlockEntity from
	 * the local variable of the currently hovered box.
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
			@Local(name = "toolboxBlockEntity") ToolboxBlockEntity toolboxBlockEntity,
			@Local(name = "ms") PoseStack ms) {
		createtoolboxtooltip$draw(graphics, mouseX, mouseY, toolboxBlockEntity, ms, 0, -(float)this.height / 2.0F);
	}

	/**
	 * Adds a persistent toolbox tooltip to the radial toolbox selection
	 * menu while a toolbox is selected, and you are viewing its contents.
	 */
	@Inject(
			method = "renderWindow",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V",
					ordinal = 4
			)
	)
	private void createtoolboxtooltip$drawToolboxTooltipOnSelectedToolbox(GuiGraphics graphics,
			 int mouseX, int mouseY, float partialTicks, CallbackInfo ci,
			 @Local(name = "ms") PoseStack ms) {
		if (this.selectedBox != null) {
			createtoolboxtooltip$draw(graphics, mouseX, mouseY, this.selectedBox, ms, (float)this.width / 2.0F, 0);
		}
	}

	/**
	 * Creates the PreviewRender, PreviewContext, and PreviewProvider
	 * given a ToolboxBlockEntity, then draws the toolbox preview
	 * for the corresponding toolbox.
	 */
	@Unique
	private void createtoolboxtooltip$draw(GuiGraphics graphics, int mouseX,
			int mouseY, ToolboxBlockEntity toolboxBlockEntity, PoseStack ms, float x, float y) {
		// check if "Radial Toolbox Menu Tooltip" config option is toggled on
		ClientConfiguration config = ShulkerBoxTooltipClient.getConfig();
		PreviewType previewType = ((PreviewCategoryAccess) config.preview)
				.createtoolboxtooltip$getRadialToolboxMenuTooltip();
		if (previewType != PreviewType.NO_PREVIEW && config.preview.enable) {
			PreviewRenderer renderer = PreviewRenderer.getDefaultRendererInstance();
			// I couldn't figure out how to get the corresponding ItemStack from the
			// toolboxBlockEntity, so instead we are grabbing the ToolboxInventory
			// (which has all the info we need) from toolboxBlockEntity and passing
			// it as a parameter to toolboxPreviewProvider.
			ToolboxPreviewProvider provider = new ToolboxPreviewProvider(
					((ToolboxBlockEntityAccess) toolboxBlockEntity)
					.createtoolboxtooltip$getToolboxInventory());
			Level world = Minecraft.getInstance().level;
			PreviewContext context = PreviewContext.builder(
					AllBlocks.TOOLBOXES.get(toolboxBlockEntity.getColor()).asStack())
					.withRegistryLookup(world == null ? null : world.registryAccess())
					.build();
			renderer.setPreview(context, provider);
			renderer.setPreviewType(previewType);
			// drawing the tooltip
			if (provider.shouldDisplay(context)) {
				float width;
				// the vanillaRenderer's getWidth() doesn't get the true
				// width of the tooltip container but getColumnsWidth() does.
				if (renderer instanceof VanillaPreviewRenderer vanillaRenderer) {
					width = ((VanillaPreviewRendererAccess) vanillaRenderer)
							.createtoolboxtooltip$getColumnsWidth();
				} else {
					width = renderer.getWidth();
				}
				// we're injecting before the previous drawing has been popped,
				// so we have to pop first instead of last.
				ms.popPose();
				ms.pushPose();
				ms.translate(x - width / 2.0F, y, 0);
				renderer.draw(0,0, graphics, this.font, mouseX, mouseY);
			}
		}
	}
}
