package com.reyzerbit.mca_reborn.common.items;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.reyzerbit.mca_reborn.common.entities.MCAVillager;
import com.reyzerbit.mca_reborn.common.enums.EnumMarriageState;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;

public class MatchmakersRing extends SpecialCaseGift {
	
    public MatchmakersRing(Properties prop) {
    	
		super(prop);
		
	}

	public boolean handle(PlayerEntity player, MCAVillager villager) {
    	
        // ensure two rings are in the inventory
        if (player.inventory.getItem(player.inventory.selected).getCount() < 2) {
            villager.say(Optional.of(player), "interaction.matchmaker.fail.needtwo");
            return false;
        }

        // ensure our target isn't married already
        if (villager.isMarried()) {
            villager.say(Optional.of(player), "interaction.matchmaker.fail.married");
            return false;
        }

        List<MCAVillager> villagers = new ArrayList<MCAVillager>();
        for(MCAVillager v : villager.level.getEntitiesOfClass(MCAVillager.class, null)) {
        	
        	if(v != null && !v.isMarried() && !v.isBaby() && v.distanceTo(villager) < 3.0D && v != villager) {
        		
        		villagers.add(v);
        		
        	}
        	
        }
        
        Optional<MCAVillager> target = villagers.stream().min(Comparator.comparingDouble(villager::distanceTo));

        // ensure we found a nearby villager
        if (!target.isPresent()) {
        	
            villager.say(Optional.of(player), "interaction.matchmaker.fail.novillagers");
            return false;
            
        }

        // setup the marriage by assigning spouse UUIDs
        MCAVillager spouse = target.get();
        villager.set(MCAVillager.SPOUSE_UUID, Optional.of(target.get().getUUID()));
        villager.set(MCAVillager.MARRIAGE_STATE, EnumMarriageState.MARRIED.getId());
        villager.set(MCAVillager.SPOUSE_NAME, spouse.get(MCAVillager.VILLAGER_NAME));
        spouse.set(MCAVillager.SPOUSE_UUID, Optional.of(villager.getUUID()));
        spouse.set(MCAVillager.MARRIAGE_STATE, EnumMarriageState.MARRIED.getId());
        spouse.set(MCAVillager.SPOUSE_NAME, villager.get(MCAVillager.VILLAGER_NAME));

        // spawn hearts to show something happened
        villager.spawnParticles(ParticleTypes.HEART);
        target.get().spawnParticles(ParticleTypes.HEART);

        // remove the rings for survival mode
        if (!player.isCreative()) player.inventory.setItem(player.inventory.selected, ItemStack.EMPTY);
        return true;
    }
}
