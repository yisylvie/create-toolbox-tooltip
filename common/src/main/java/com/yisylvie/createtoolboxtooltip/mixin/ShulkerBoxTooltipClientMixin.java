package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.access.PreviewCategoryAccess;
import com.yisylvie.createtoolboxtooltip.api.ToolboxPreviewProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip;
import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltipClient;
import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.PreviewType;
import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Collections;
import java.util.List;

/**
 * If an inventory is empty, but not the compartments,
 * we do not display anything in compact mode, but we do display
 * in full mode. Here, we change the keybind tooltips accordingly.
 */
@Mixin(ShulkerBoxTooltipClient.class)
public abstract class ShulkerBoxTooltipClientMixin {

	/**
     * Changes the previewKeyHint.append(previewKeyText) operation
     *
     * @return the new preview key hint displayed when isInventoryEmpty()
     */
    @WrapOperation(
        method = "getPreviewKeyTooltipHint",
        at = @At(
            value = "INVOKE",
			target = "Lnet/minecraft/network/chat/MutableComponent;append(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;",
            ordinal = 2
        )
//		remap = false,
//		require = 0
    )
    private static MutableComponent createtoolboxtooltip$changePreviewKeyHint(
            MutableComponent previewKeyHint,
            Component previewKeyText,
            Operation<MutableComponent> previewKeyHintOperation,
            @Local(argsOnly = true) PreviewProvider provider,
            @Local(argsOnly = true) PreviewContext context) {
        if (provider instanceof ToolboxPreviewProvider) {
            ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider) provider;
            if (toolboxProvider.isInventoryEmpty(context)
                    && !ShulkerBoxTooltip.config.preview.swapModes) {
                Component fullPreviewKey = ShulkerBoxTooltip
                        .config.controls.fullPreviewKey.get().getDisplayName();
                String newHint = fullPreviewKey.getString();
                if (!ShulkerBoxTooltip.config.preview.alwaysOn) {
                    newHint += "+" + previewKeyText.getString();
                    return previewKeyHintOperation.call(
                        previewKeyHint, Component.nullToEmpty(newHint));
                }
                return previewKeyHintOperation.call(
                        previewKeyHint,
                        ShulkerBoxTooltip.config.controls.fullPreviewKey.get().getDisplayName());
            }
        }
        return previewKeyHintOperation.call(previewKeyHint, previewKeyText);
    }

    /**
     * Since there is no compact mode when isInventoryEmpty, we never
     * want to display a keybind tooltip when the preview type is full
     */
    @Inject(
        method = "getPreviewKeyTooltipHint",
        at = @At(
            value = "HEAD"
        ),
        cancellable = true
//		remap = false,
//		require = 0
    )
    private static void createtoolboxtooltip$removePreviewKeyHint(
            PreviewContext context, PreviewProvider provider, boolean previewRequested,
            CallbackInfoReturnable<Component> cir) {
        if (provider instanceof ToolboxPreviewProvider) {
            ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider) provider;
            if (toolboxProvider.isInventoryEmpty(context)
                    && ShulkerBoxTooltipApi.getCurrentPreviewType(provider.isFullPreviewAvailable(context))
                        == PreviewType.FULL) {
                cir.setReturnValue(null);
            }
        }
	}

    /**
     * Set to only ever see "view full contents" when isInventoryEmpty()
     */
    @ModifyArg(
        method = "getPreviewKeyTooltipHint",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"
        )
//		remap = false,
//		require = 0
    )
    private static String createtoolboxtooltip$changeContentHint(
            String contentHint,
            @Local(argsOnly = true) PreviewContext context,
            @Local(argsOnly = true) PreviewProvider provider) {
        if (provider instanceof ToolboxPreviewProvider) {
            ToolboxPreviewProvider toolboxProvider = (ToolboxPreviewProvider) provider;
            if (toolboxProvider.isInventoryEmpty(context)) {
                return provider.getFullTooltipHintLangKey(context);
            }
        }
        return contentHint;
    }

	/**
	 * We don't want to display keybind tooltips for other container
	 * types when "enable only toolboxes" is set to true
	 */
	@Inject(
			method = "getTooltipHints",
			at = @At(
				value = "HEAD"
			),
			remap = false,
			cancellable = true
//			require = 0
	)
	private static void createtoolboxtooltip$previewTooltipAvailableWithToolboxes(
			PreviewContext context,
			PreviewProvider provider,
			CallbackInfoReturnable<List<Component>> cir) {
		if (((PreviewCategoryAccess) ShulkerBoxTooltip.config.preview).createtoolboxtooltip$getEnableOnlyToolboxes()
				&& !(provider instanceof ToolboxPreviewProvider)) {
			cir.setReturnValue(Collections.emptyList());
		}
	}

	/**
	 * We want to turn off the Shulker Box tooltips for everything but
	 * toolboxes if createtoolboxtooltip$enableOnlyToolBoxes is set to true
	 */
	@Inject(
			method = "isPreviewAvailable",
			at = @At(
					value = "HEAD"
			),
			cancellable = true,
			remap = false
	)
	private static void createtoolboxtooltip$previewAvailableWithToolboxes(
			PreviewContext context, CallbackInfoReturnable<Boolean> cir) {
		PreviewProvider provider = ShulkerBoxTooltipApi.getPreviewProviderForStack(context.stack());
		if (((PreviewCategoryAccess) ShulkerBoxTooltip.config.preview).createtoolboxtooltip$getEnableOnlyToolboxes()
				&& !(provider instanceof ToolboxPreviewProvider)) {
			cir.setReturnValue(false);
		}
	}
}
