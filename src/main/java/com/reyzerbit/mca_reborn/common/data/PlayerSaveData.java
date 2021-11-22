package com.reyzerbit.mca_reborn.common.data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.reyzerbit.mca_reborn.common.Constants;
import com.reyzerbit.mca_reborn.common.MCA;
import com.reyzerbit.mca_reborn.common.enums.EnumMarriageState;

import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

@Getter
public class PlayerSaveData extends WorldSavedData {
	
    public static final String SAVED_DATA_NAME = "MCAPlayerData";

    List<PlayerData> PLAYER_DATA;

    public PlayerSaveData() {
    	
        super(SAVED_DATA_NAME);
        
        PLAYER_DATA = new ArrayList<PlayerData>();
        
    }

    public Optional<PlayerData> get(UUID player) {
    	
    	for(PlayerData data : PLAYER_DATA) {
    		
    		if(data.getPlayer().equals(player)) {
    			
    			return Optional.of(data);
    			
    		}
    		
    	}
    	
    	return Optional.empty();
    	
    }

    public static PlayerSaveData getExisting(World world, UUID uuid) {
    	
        return null; //(PlayerSaveData) world.getLevelData(PlayerSaveData.class, PREFIX + uuid.toString());
        
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
    	
    	for(PlayerData data : PLAYER_DATA) {
        	
        	CompoundNBT playerData = new CompoundNBT();
        	
        	playerData.putUUID("spouseUUID", data.getSpouseUUID());
        	playerData.putInt("marriageState", data.getMarriageState().getId());
        	playerData.putString("spouseName", data.getSpouseName());
        	playerData.putBoolean("babyPresent", data.isBabyPresent());
        	
        	nbt.put(data.getPlayer().toString(), playerData);
    		
    	}
    	
        return nbt;
        
    }

    @Override
    public void load(CompoundNBT nbt) {
    	
    	for(String nbtLocation : nbt.getAllKeys()) {
    		
    		CompoundNBT playerData = nbt.getCompound(nbtLocation);
    		
    		if(playerData == null) return;
    		
    		PLAYER_DATA.add(new PlayerData(UUID.fromString(nbtLocation), playerData.getUUID("spouseUUID"), EnumMarriageState.byId(playerData.getInt("marriageState")),
    				playerData.getString("spouseName"), playerData.getBoolean("babyPresent")));
    		
    	}
        
    }

    public Optional<Boolean> isMarriedOrEngaged(UUID player) {
    	
    	for(PlayerData data : PLAYER_DATA) {
    		
    		if(data.getPlayer().equals(player)) {
    			
    			return Optional.of(data.getMarriageState() != EnumMarriageState.NOT_MARRIED);
    			
    		}
    		
    	}
    	
    	return Optional.empty();
        
    }

    public void marry(UUID player, UUID uuid, String name) {
    	
    	for(PlayerData data : PLAYER_DATA) {
    		
    		if(data.getPlayer().equals(player)) {
    			
    			data.setSpouseUUID(uuid);
    			data.setMarriageState(EnumMarriageState.MARRIED);
    			data.setSpouseName(name);
    			
    		}
    		
    	}
        
        setDirty();
        
    }

    public void endMarriage(UUID player) {
    	
    	for(PlayerData data : PLAYER_DATA) {
    		
    		if(data.getPlayer().equals(player)) {
    			
    			data.setSpouseUUID(Constants.ZERO_UUID);
    			data.setMarriageState(EnumMarriageState.NOT_MARRIED);
    			data.setSpouseName("");
    			
    		}
    		
    	}
        
        setDirty();
        
    }

    public void setBabyPresent(UUID player, boolean value) {

    	for(PlayerData data : PLAYER_DATA) {
    		
    		if(data.getPlayer().equals(player)) {
    			
    			data.setBabyPresent(value);
    			
    		}
    		
    	}
    	
        setDirty();
        
    }

    public void reset(UUID player) {
    	
        endMarriage(player);
        setBabyPresent(player, false);
        setDirty();
        
    }

    public List<Field> getDataFields() {
    	
        return Arrays.stream(this.getClass().getDeclaredFields()).filter(f -> !Modifier.isFinal(f.getModifiers())).collect(Collectors.toList());
        
    }

    public void dump(PlayerEntity player) {
    	
        for (Field f : getDataFields()) {
        	
            try {
            	
                player.sendMessage(new StringTextComponent(f.getName() + " = " + f.get(this).toString()), player.getUUID());
                
            } catch (Exception e) {
            	
                MCA.getLog().error("Error dumping player data!");
                MCA.getLog().error(e);
                
            }
            
        }
        
    }
    
}
