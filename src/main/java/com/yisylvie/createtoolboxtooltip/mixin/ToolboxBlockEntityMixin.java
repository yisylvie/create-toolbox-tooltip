package com.yisylvie.createtoolboxtooltip.mixin;

import com.yisylvie.createtoolboxtooltip.access.ToolboxBlockEntityAccess;

import com.simibubi.create.content.equipment.toolbox.ToolboxBlockEntity;
import com.simibubi.create.content.equipment.toolbox.ToolboxInventory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ToolboxBlockEntity.class)
 public abstract class ToolboxBlockEntityMixin implements ToolboxBlockEntityAccess {

	@Shadow
	ToolboxInventory inventory;

	@Unique
	public ToolboxInventory createtoolboxtooltip$getToolboxInventory() {
		return this.inventory;
	}
}
