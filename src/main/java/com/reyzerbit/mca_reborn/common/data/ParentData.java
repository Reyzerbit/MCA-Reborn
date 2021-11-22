package com.reyzerbit.mca_reborn.common.data;

import java.util.UUID;

import com.reyzerbit.mca_reborn.common.Constants;
import com.reyzerbit.mca_reborn.common.entities.MCAVillager;


import static com.reyzerbit.mca_reborn.common.entities.MCAVillager.*;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

@Getter
public class ParentData {
    private UUID parent1UUID = Constants.ZERO_UUID;
    private UUID parent2UUID = Constants.ZERO_UUID;
    private String parent1Name = "";
    private String parent2Name = "";

    public static ParentData fromNBT(CompoundNBT nbt) {
    	
        ParentData data = new ParentData();
        data.parent1UUID = nbt.getUUID("parent1UUID");
        data.parent2UUID = nbt.getUUID("parent2UUID");
        data.parent1Name = nbt.getString("parent1Name");
        data.parent2Name = nbt.getString("parent2Name");
        return data;
        
    }

    public static ParentData create(UUID parent1UUID, UUID parent2UUID, String parent1Name, String parent2Name) {
    	
        ParentData data = new ParentData();
        data.parent1UUID = parent1UUID;
        data.parent2UUID = parent2UUID;
        data.parent1Name = parent1Name;
        data.parent2Name = parent2Name;
        return data;
        
    }

    public static ParentData fromVillager(MCAVillager villager) {
    	
        ParentData data = new ParentData();
        data.parent1Name = villager.get(VILLAGER_NAME);
        data.parent1UUID = villager.getUUID();
        data.parent2Name = villager.get(SPOUSE_NAME);
        data.parent2UUID = villager.get(SPOUSE_UUID).orElse(Constants.ZERO_UUID);
        return data;
        
    }

    public CompoundNBT toNBT() {
    	
    	CompoundNBT nbt = new CompoundNBT();
        nbt.putUUID("parent1UUID", parent1UUID);
        nbt.putUUID("parent2UUID", parent2UUID);
        nbt.putString("parent1Name", parent1Name);
        nbt.putString("parent2Name", parent2Name);
        return nbt;
        
    }

    public ParentData setParents(UUID parent1UUID, String parent1Name, UUID parent2UUID, String parent2Name) {
    	
        this.parent1UUID = parent1UUID;
        this.parent2UUID = parent2UUID;
        this.parent1Name = parent1Name;
        this.parent2Name = parent2Name;
        return this;
        
    }

    public Entity getParentEntity(World world, UUID uuid) {
    	
    	// TODO: This should definitely be changed to an optional
    	Entity playerParentEntity = world.getLoadedEntitiesOfClass(PlayerEntity.class, null).stream().filter(e -> e.getUUID().equals(uuid)).findFirst().orElse(null);
    	Entity villagerParentEntity = world.getLoadedEntitiesOfClass(MCAVillager.class, null).stream().filter(e -> e.getUUID().equals(uuid)).findFirst().orElse(null);
    	
    	if(playerParentEntity == null) {
    		
    		return villagerParentEntity;
    		
    	}
    	
    	return playerParentEntity;
        
    }

    public Entity[] getParentEntities(World world) {
    	
        return new Entity[]{
        		
                getParentEntity(world, getParent1UUID()),
                getParentEntity(world, getParent2UUID())
                
        };
        
    }
    
}
