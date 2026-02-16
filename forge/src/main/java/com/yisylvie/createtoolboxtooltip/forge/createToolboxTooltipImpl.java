package com.yisylvie.createtoolboxtooltip.forge;

import com.yisylvie.createtoolboxtooltip.createToolboxTooltipPlugin;
import com.misterpemodder.shulkerboxtooltip.api.forge.ShulkerBoxTooltipPlugin;
import com.yisylvie.createtoolboxtooltip.createToolboxTooltip;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(createToolboxTooltip.ID)
public class createToolboxTooltipImpl {
    public createToolboxTooltipImpl() {
        // plugin registration
        ModLoadingContext.get().registerExtensionPoint(ShulkerBoxTooltipPlugin.class,
                () -> new ShulkerBoxTooltipPlugin(createToolboxTooltipPlugin::new));
    }
}
