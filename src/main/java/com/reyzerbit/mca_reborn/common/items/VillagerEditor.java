package com.reyzerbit.mca_reborn.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class VillagerEditor extends Item {

    public VillagerEditor(Properties prop) {
    	
        super(prop);
        
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
    	
        return true;
        
    }
}