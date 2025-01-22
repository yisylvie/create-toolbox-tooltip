package com.misterpemodder.shulkerboxtooltip.api;

import com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltipClient;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorRegistry;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry;
import com.misterpemodder.shulkerboxtooltip.impl.network.ServerNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class toolboxShulkerBoxTooltipPlugin implements ShulkerBoxTooltipApi {
    @Override
    public void registerProviders(PreviewProviderRegistry registry) {
    registry.register(new Identifier("createtoolboxtooltip", "barrel_example"),
        new BlockEntityPreviewProvider(27, true), Items.BARREL);
    }
}