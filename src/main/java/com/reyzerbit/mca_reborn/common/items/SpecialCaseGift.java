package com.reyzerbit.mca_reborn.common.items;

import com.reyzerbit.mca_reborn.common.entities.MCAVillager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

public abstract class SpecialCaseGift extends Item {
	
    public SpecialCaseGift(Properties prop) { super(prop); }

	public abstract boolean handle(PlayerEntity player, MCAVillager villager);
    
}
