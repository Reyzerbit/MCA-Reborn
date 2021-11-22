package com.reyzerbit.mca_reborn.common.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

/**
 * SavedVillagers handles saving dead villagers to the world which allows them to be restored later.
 */
public class SavedVillagers extends WorldSavedData {

    private static final String DATA_ID = "MCA-Villagers";
    private Map<String, CompoundNBT> villagerData = new HashMap<>();

    public SavedVillagers() {
    	
        super(DATA_ID);
        
    }

	@Override
	public void load(CompoundNBT nbt) {
		
		nbt.getAllKeys().forEach((k) -> villagerData.put(k, nbt.getCompound(k)));
		
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		
		villagerData.forEach(nbt::put);
        return nbt;
        
	}

	// Replaced original usage with DimensionSavedDataManager#computeIfAbsent
	
    public static SavedVillagers getData() {
        
        return new SavedVillagers();
        
    }

    public void remove(UUID uuid) {
    	
        villagerData.remove(uuid.toString());
        setDirty();
        
    }

    public Map<String, CompoundNBT> getMap() {
    	
        return villagerData;
        
    }

    public CompoundNBT loadByUUID(UUID uuid) {
    	
        return villagerData.get(uuid.toString());
        
    }
    
}
