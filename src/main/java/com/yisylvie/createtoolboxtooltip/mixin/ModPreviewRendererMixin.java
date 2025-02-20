package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.createToolboxTooltip;
import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import java.util.List;

import javax.tools.Tool;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.PreviewType;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import com.misterpemodder.shulkerboxtooltip.api.renderer.PreviewRenderer;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer;
import com.misterpemodder.shulkerboxtooltip.impl.renderer.ModPreviewRenderer;
import com.misterpemodder.shulkerboxtooltip.impl.util.MergedItemStack;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour.Base;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

@Mixin(ModPreviewRenderer.class)
public abstract class ModPreviewRendererMixin extends BasePreviewRenderer{
    protected ModPreviewRendererMixin(int slotWidth, int slotHeight, int slotXOffset, int slotYOffset) {
        super(slotWidth, slotHeight, slotXOffset, slotYOffset);
    }

    // Mod default theme doesn't display if inventory is empty, 
    // so we must override for when there are nonempty compartments
    // and the preview type is full 
    @ModifyExpressionValue(
        method = "draw", 
        at = @At(
            value = "INVOKE", 
            target = "Ljava/util/List;isEmpty()Z",
            ordinal = 0
        )
    )
    private boolean createtoolboxtooltip$drawIfEmpty(boolean isEmpty) {
        if (this.provider instanceof ToolboxPreviewProvider) {
            if(this.previewType == PreviewType.FULL) {
                ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider)(this.provider);
                // We must (double) negate since isEmpty is negated in original if statement
                return !toolboxProvider.shouldDisplay(this.previewContext);
            }
        }
        return isEmpty;
    }
}