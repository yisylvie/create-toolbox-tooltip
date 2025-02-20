package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.createToolboxTooltip;
import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.mutable.Mutable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;
import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltipClient;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin(ShulkerBoxTooltipClient.class)
public abstract class ShulkerBoxTooltipClientMixin {
    // @Shadow(remap = false)
    // protected PreviewConfiguration config;
    
    @WrapOperation(
        method = "getPreviewKeyTooltipHint", 
        at = @At(
            value = "INVOKE",
			target = "Lnet/minecraft/network/chat/MutableComponent;append(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;",
            ordinal = 2
        )
    // cancellable=true,
    // locals = LocalCapture.CAPTURE_FAILHARD
    ) 
    private static MutableComponent createtoolboxtooltip$changePreviewKeyHint(
            MutableComponent previewKeyHint,
            Component previewKeyText, 
            Operation<MutableComponent> previewKeyHintOperation,
            @Local(argsOnly = true) PreviewProvider provider,
            @Local(argsOnly = true) PreviewContext context
            // @Local MutableComponent previewKeyHint
            ) {
        if (provider instanceof ToolboxPreviewProvider) {
            ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider)provider;
            if (ToolboxPreviewProvider.getItemCount(toolboxProvider.getInventory(context)) == 0) {
                // createToolboxTooltip.LOGGER.info("[{}] toolboxing deez nutz! previewKey:"
                //         + provider.getInventory(context),
                //         createToolboxTooltip.NAME, Create.VERSION);
                Component fullPreviewKey = ShulkerBoxTooltip.config.controls.fullPreviewKey.get().getDisplayName();
                String newHint = fullPreviewKey.getString();
                if (!ShulkerBoxTooltip.config.preview.alwaysOn) {
                    // previewKeyHintOperation.call(
                    //     previewKeyHint, 
                    //     ShulkerBoxTooltip.config.controls.fullPreviewKey.get().getDisplayName());
                    newHint += "+" + previewKeyText.getString();
                    return previewKeyHintOperation.call(previewKeyHint, Component.nullToEmpty(newHint));
                }

                return previewKeyHintOperation.call(previewKeyHint, 
                        ShulkerBoxTooltip.config.controls.fullPreviewKey.get().getDisplayName());
            }
        }
        return previewKeyHintOperation.call(previewKeyHint, previewKeyText);
    }
}