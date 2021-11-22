package com.reyzerbit.mca_reborn.common.items;

import java.util.Optional;

import com.reyzerbit.mca_reborn.common.data.PlayerHistory;
import com.reyzerbit.mca_reborn.common.data.PlayerSaveData;
import com.reyzerbit.mca_reborn.common.entities.MCAVillager;
import com.reyzerbit.mca_reborn.common.enums.EnumDialogueType;
import com.reyzerbit.mca_reborn.common.util.MCAConfig;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;

public class WeddingRing extends SpecialCaseGift {
	
    public WeddingRing(Properties prop) { super(prop); }

	public boolean handle(PlayerEntity player, MCAVillager villager) {
		
        PlayerSaveData savedData = player.level.getServer().overworld().getChunkSource().getDataStorage().computeIfAbsent(PlayerSaveData::new, PlayerSaveData.SAVED_DATA_NAME);
        PlayerHistory history = villager.getPlayerHistoryFor(player.getUUID());
        String response;

        if (villager.isMarriedTo(player.getUUID()))
            response = "interaction.marry.fail.marriedtogiver";
        
        else if (villager.isMarried())
            response = "interaction.marry.fail.marriedtoother";
        
        else if (savedData.isMarriedOrEngaged(player.getUUID()).get())
            response = "interaction.marry.fail.marriedtoother";
        
        else if (this instanceof EngagementRing && history.getHearts() < MCAConfig.marriageHeartsRequirement.get() / 2)
            response = "interaction.marry.fail.lowhearts";
        
        else if (!(this instanceof EngagementRing) && history.getHearts() < MCAConfig.marriageHeartsRequirement.get())
            response = "interaction.marry.fail.lowhearts";
        
        else {
        	
            response = "interaction.marry.success";
            savedData.marry(player.getUUID(), villager.getUUID(), villager.get(MCAVillager.VILLAGER_NAME));
            villager.getPlayerHistoryFor(player.getUUID()).setDialogueType(EnumDialogueType.SPOUSE);
            villager.spawnParticles(ParticleTypes.HEART);
            villager.marry(player);
            
        }

        villager.say(Optional.of(player), response);
        return false;
        
    }
    
}
