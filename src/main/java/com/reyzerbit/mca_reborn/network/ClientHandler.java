package com.reyzerbit.mca_reborn.network;

import java.util.Optional;

import com.reyzerbit.mca_reborn.client.gui.StaffOfLifeScreen;
import com.reyzerbit.mca_reborn.client.gui.WhistleScreen;
import com.reyzerbit.mca_reborn.common.MCA;
import com.reyzerbit.mca_reborn.common.entities.MCAVillager;
import com.reyzerbit.mca_reborn.network.MCAMessages.CareerResponse;
import com.reyzerbit.mca_reborn.network.MCAMessages.GetFamilyResponse;
import com.reyzerbit.mca_reborn.network.MCAMessages.InventoryResponse;
import com.reyzerbit.mca_reborn.network.MCAMessages.SavedVillagersResponse;
import com.reyzerbit.mca_reborn.network.MCAMessages.Say;
import com.reyzerbit.mca_reborn.network.MCAMessages.SpawnParticles;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ClientHandler {

    public static void handleMessage(MCAMessages.ButtonAction message, ServerPlayerEntity player) {
    	
        switch (message.getButtonId()) {
            case "gui.button.debug.startraid":
                startRaid(player);
                break;
            case "gui.button.debug.spawnguards":
                spawnGuards(player);
                break;
            case "gui.button.debug.rebuildvillage":
                rebuildVillage(player);
                break;
        }
        
    }
    
    public static void handleMessage(SpawnParticles message, PlayerEntity player) {
    	
        World world = player.level;
        		
        Optional<MCAVillager> entity = world.getLoadedEntitiesOfClass(MCAVillager.class, null).stream().filter((e) -> e.getUUID().equals(message.getEntityUUID())).findFirst();
        if (!entity.isPresent()) return;
        
        MCAVillager villager = entity.get();
        villager.spawnParticles(message.getParticleType());
        
        return;
        
    }
    
    public static void handleMessage(Say message, PlayerEntity player) {
    	
        MCAVillager villager = (MCAVillager) player.level.getEntity(message.getSpeakingEntityId());

        if (villager != null) villager.say(Optional.of((PlayerEntity)player), message.getPhraseId());

        return;
        
    }
    
    public static void handleMessage(InventoryResponse message, PlayerEntity player) {
        
        if (player != null) {
        	
        	MCAVillager villager = (MCAVillager) player.level.getEntity(message.getEntityUUID().hashCode());
            villager.inventory.readInventoryFromNBT(message.getInventoryNBT());
            
        }
        
    }
    
    public static void handleMessage(CareerResponse msg, PlayerEntity player) {

        try {
        	
            MCAVillager villager = (MCAVillager) player.level.getEntity(msg.getEntityUUID().hashCode());

            ObfuscationReflectionHelper.setPrivateValue(VillagerEntity.class, villager, msg.getCareerId(), MCAVillager.VANILLA_CAREER_ID_FIELD_INDEX);
            
        } catch (ClassCastException e) {
            MCA.getLog().error("Failed to cast entity to villager on career ID update.");
        } catch (Exception e) {
            MCA.getLog().error("Failed to set career ID on villager!", e);
        }
        
    }
    
    public static void handleMessage(SavedVillagersResponse msg, ServerPlayerEntity player) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof StaffOfLifeScreen) ((StaffOfLifeScreen) mc.screen).setVillagerData(msg.getVillagers());
        return;
        
    }
    
    public static void handleMessage(GetFamilyResponse msg, ServerPlayerEntity player) {
    	
        Minecraft mc = Minecraft.getInstance();
        
        if (mc.screen instanceof WhistleScreen) {
        	
            WhistleScreen whistleScreen = (WhistleScreen)mc.screen;
            whistleScreen.setVillagerDataList(msg.getFamilyData());
            
        }
        
        return;
        
    }

    private static void startRaid(ServerPlayerEntity player) {
    	
        player.sendMessage(new StringTextComponent("Starting raid on village..."), player.getUUID());
        //TODO Fix force raid
        //VillageHelper.forceRaid(player);
        
    }

    private static void spawnGuards(ServerPlayerEntity player) {
    	
        player.sendMessage(new StringTextComponent("Spawning village guards..."), player.getUUID());
        //VillageHelper.tick(player.getLevel());
        
    }

    private static void rebuildVillage(ServerPlayerEntity player) {
    	
        player.sendMessage(new StringTextComponent("Rebuilding annihilated village..."), player.getUUID());
        
    }
    
}
