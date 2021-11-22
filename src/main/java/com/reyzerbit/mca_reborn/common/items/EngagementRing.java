package com.reyzerbit.mca_reborn.common.items;

import java.util.List;

import javax.annotation.Nullable;

import com.reyzerbit.mca_reborn.common.entities.MCAVillager;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EngagementRing extends WeddingRing {
	
    public EngagementRing(Properties prop) {
    	
		super(prop);
		
	}

	public boolean handle(PlayerEntity player, MCAVillager villager) {
    	
        return super.handle(player, villager);
        
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    	
        tooltip.add("Halves the hearts required to marry someone.");
        
    }
    
}
