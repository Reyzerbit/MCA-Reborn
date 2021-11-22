package com.reyzerbit.mca_reborn.common.data;

import java.util.UUID;

import com.reyzerbit.mca_reborn.common.Constants;
import com.reyzerbit.mca_reborn.common.entities.MCAVillager;
import com.reyzerbit.mca_reborn.common.enums.EnumDialogueType;

import lombok.Getter;
import net.minecraft.nbt.CompoundNBT;

public class PlayerHistory {
	
    @Getter private int hearts;
    @Getter private int interactionFatigue;
    @Getter private boolean giftPresent;
    @Getter private int greetTimer;
    @Getter private EnumDialogueType dialogueType;

    @Getter private UUID playerUUID;
    private MCAVillager villager;

    private PlayerHistory() {
    	
        hearts = 0;
        interactionFatigue = 0;
        giftPresent = false;
        greetTimer = 0;
        playerUUID = Constants.ZERO_UUID;
        dialogueType = EnumDialogueType.ADULT;
        
    }

    public static PlayerHistory getNew(MCAVillager villager, UUID uuid) {
    	
        PlayerHistory history = new PlayerHistory();
        history.villager = villager;
        history.playerUUID = uuid;

        if (villager.isBaby()) {
        	
            history.setDialogueType(EnumDialogueType.CHILD);
            
        } else {
        	
            history.setDialogueType(EnumDialogueType.ADULT);
            
        }
        
        return history;
        
    }

    public static PlayerHistory fromNBT(MCAVillager villager, UUID uuid, CompoundNBT nbt) {
    	
        PlayerHistory history = new PlayerHistory();
        history.villager = villager;
        history.playerUUID = uuid;

        history.hearts = nbt.getInt("hearts");
        history.interactionFatigue = nbt.getInt("interactionFatigue");
        history.giftPresent = nbt.getBoolean("giftPresent");
        history.greetTimer = nbt.getInt("greetTimer");
        history.dialogueType = EnumDialogueType.byValue(nbt.getString("dialogueType"));

        return history;
        
    }

    public CompoundNBT toNBT() {
    	
    	CompoundNBT nbt = new CompoundNBT();

        nbt.putInt("hearts", hearts);
        nbt.putInt("interactionFatigue", interactionFatigue);
        nbt.putBoolean("giftPresent", giftPresent);
        nbt.putInt("greetTimer", greetTimer);
        nbt.putString("dialogueType", dialogueType.getId());

        return nbt;
        
    }

    public void setHearts(int value) {
    	
        hearts = value;
        villager.updatePlayerHistoryMap(this);
        
    }

    public void changeHearts(int value) {
    	
        hearts += value;
        villager.updatePlayerHistoryMap(this);
        
    }

    public void changeInteractionFatigue(int value) {
    	
        interactionFatigue += value;
        villager.updatePlayerHistoryMap(this);
        
    }

    public void update() {
    	
        // every 5 minutes reduce interaction fatigues
        if (villager.tickCount % 6000 == 0) changeInteractionFatigue(-1);
        
    }

    public void setDialogueType(EnumDialogueType type) {
    	
        this.dialogueType = type;
        villager.updatePlayerHistoryMap(this);
        
    }
}
