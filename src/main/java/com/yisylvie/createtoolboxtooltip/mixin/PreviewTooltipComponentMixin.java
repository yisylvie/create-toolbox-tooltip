package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.createToolboxTooltip;
import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import java.util.Iterator;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;
import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.impl.tooltip.PreviewTooltipComponent;
import com.misterpemodder.shulkerboxtooltip.api.PreviewType;
import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.config.PreviewConfiguration;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import com.misterpemodder.shulkerboxtooltip.api.renderer.PreviewRenderer;
import com.misterpemodder.shulkerboxtooltip.impl.config.Configuration.PreviewPosition;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.VanillaPreviewRenderer;
import com.misterpemodder.shulkerboxtooltip.impl.util.MergedItemStack;
import com.simibubi.create.Create;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin(PreviewTooltipComponent.class)
public abstract class PreviewTooltipComponentMixin {
    @Shadow(remap = false)
    private PreviewRenderer renderer;

    @Shadow(remap = false)
    private PreviewProvider provider;

    @Shadow(remap = false)
    private PreviewContext context;
    
    // We want tooltip hints but not the inventory to render
    // when the preview type is compact and the inventory is empty 
    // but the compartments are not. Doing so causes there to be 
    // a silly little gap where the inventory would have been displayed.
    // In this instance, we must override getHeight() to close that gap
    @Inject(
        method = "getHeight", 
        at = @At(
            value = "RETURN"
        ),
        cancellable = true
    ) 
    private void createtoolboxtooltip$changeHeight(CallbackInfoReturnable<Integer> cir) {
        if (provider instanceof ToolboxPreviewProvider) {
            ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider)provider;
            if (ShulkerBoxTooltip.config.preview.position == PreviewPosition.INSIDE 
                    && ShulkerBoxTooltipApi.getCurrentPreviewType(provider.isFullPreviewAvailable(context)) == PreviewType.COMPACT
                    && ToolboxPreviewProvider.getItemCount(toolboxProvider.getInventory(context)) == 0 
                    && ToolboxPreviewProvider.getItemCount(toolboxProvider.getCompartments(context)) != 0) {
                cir.setReturnValue(0);
            }
        }
        return;
    }
}
