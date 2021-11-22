package com.reyzerbit.mca_reborn.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.logging.log4j.Level;

import com.reyzerbit.mca_reborn.common.MCA;
import com.reyzerbit.mca_reborn.common.data.SavedVillagers;
import com.reyzerbit.mca_reborn.common.entities.MCAVillager;
import com.reyzerbit.mca_reborn.common.items.Baby;
import com.reyzerbit.mca_reborn.network.MCAMessages.BabyName;
import com.reyzerbit.mca_reborn.network.MCAMessages.ButtonAction;
import com.reyzerbit.mca_reborn.network.MCAMessages.CallToPlayer;
import com.reyzerbit.mca_reborn.network.MCAMessages.CareerRequest;
import com.reyzerbit.mca_reborn.network.MCAMessages.CareerResponse;
import com.reyzerbit.mca_reborn.network.MCAMessages.GetFamily;
import com.reyzerbit.mca_reborn.network.MCAMessages.GetFamilyResponse;
import com.reyzerbit.mca_reborn.network.MCAMessages.InventoryRequest;
import com.reyzerbit.mca_reborn.network.MCAMessages.InventoryResponse;
import com.reyzerbit.mca_reborn.network.MCAMessages.ReviveVillager;
import com.reyzerbit.mca_reborn.network.MCAMessages.SavedVillagersRequest;
import com.reyzerbit.mca_reborn.network.MCAMessages.SavedVillagersResponse;
import com.reyzerbit.mca_reborn.network.MCAMessages.SetName;
import com.reyzerbit.mca_reborn.network.MCAMessages.SetProfession;
import com.reyzerbit.mca_reborn.network.MCAMessages.SetTexture;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ServerHandler {
	
	protected static void processMessage(BabyName msg, ServerPlayerEntity player) {
		
		MCA.getLog().log(Level.INFO, "Recieved baby naming message succesfully!");
	
	    ItemStack stack = player.getMainHandItem();
	
	    if (stack.getItem() instanceof Baby) {
	    
	    	stack.getTag().putString("name", msg.getBabyName());
	    	
	    }
	
	    return;
		
	}
	
	//TODO Figure out if this is in the right spot
	protected static void processMessage(ButtonAction msg, ServerPlayerEntity player) {

        // The message can target a particular villager, or the server itself.
        if (!msg.targetsServer()) {
        	
        	MCAVillager villager = (MCAVillager) player.getLevel().getEntity(msg.getTargetUUID());
            if (villager != null) villager.handleButtonClick(player, msg.getGuiKey(), msg.getButtonId());
            
        } else {
        	
        	ClientHandler.handleMessage(msg, player);
        	
        }
        
        return;
		
	}
	
	protected static CareerResponse processMessage(CareerRequest msg, ServerPlayerEntity player) {
		
        int careerId = -255;

        try {
        	
        	MCAVillager villager = (MCAVillager) player.getLevel().getEntity(msg.getEntityUUID());

            if (villager != null) careerId = ObfuscationReflectionHelper.getPrivateValue(VillagerEntity.class, villager, MCAVillager.VANILLA_CAREER_ID_FIELD_INDEX);
            
        } catch (ClassCastException ignored) {
        	
            MCA.getLog().error("UUID provided in career request does not match an MCA villager!: " + msg.getEntityUUID().toString());
            return null;
            
        } catch (NullPointerException ignored) {
        	
            MCA.getLog().error("UUID provided in career request does not match a loaded MCA villager!: " + msg.getEntityUUID().toString());
            return null;
            
        }

        if (careerId == -255) {
        	
            MCA.getLog().error("Career ID wasn't assigned for UUID: " + msg.getEntityUUID().toString());
            return null;
            
        }

        return new CareerResponse(careerId, msg.getEntityUUID());
		
	}

	protected static InventoryResponse processMessage(InventoryRequest msg, ServerPlayerEntity player) {
		
        MCAVillager villager = (MCAVillager) player.getLevel().getEntity(msg.getEntityUUID());
        if (villager != null && villager.inventory != null) return new InventoryResponse(villager.getUUID(), villager.inventory);
        return null;
		
	}
	
	protected static SavedVillagersResponse processMessage(SavedVillagersRequest msg, ServerPlayerEntity player) {
		
        return new SavedVillagersResponse(player);
		
	}

	protected static void processMessage(ReviveVillager msg, ServerPlayerEntity player) {
		
        SavedVillagers villagers = player.getLevel().getDataStorage().computeIfAbsent(SavedVillagers::getData, "MCA-Villagers");
        CompoundNBT nbt = villagers.loadByUUID(msg.getTargetUUID());
        
        if (nbt != null) {
        	
            MCAVillager villager = new MCAVillager(EntityType.VILLAGER, player.getLevel());
            villager.setPos(player.getX(), player.getY(), player.getZ());
            player.getLevel().addFreshEntity(villager);

            villager.readAdditionalSaveData(nbt);
            villager.reset();

            villagers.remove(msg.getTargetUUID());
            player.inventory.getSelected().setDamageValue(player.inventory.getSelected().getDamageValue() - 1);
            
        }

        return;
		
	}
	
	protected static void processMessage(SetName msg, ServerPlayerEntity player) {
		
        ServerWorld world = player.getLevel();
        Optional<MCAVillager> entity = world.getLoadedEntitiesOfClass(MCAVillager.class, null).stream().filter((e) -> e.getUUID().equals(msg.getEntityUUID())).findFirst();
        
        if (!entity.isPresent()) return;
        
        MCAVillager villager = entity.get();
        villager.set(MCAVillager.VILLAGER_NAME, msg.getName());
        
        return;
		
	}
	
	protected static GetFamilyResponse processMessage(GetFamily msg, ServerPlayerEntity player) {
		
        List<MCAVillager> villagers = new ArrayList<>();
        List<CompoundNBT> familyData = new ArrayList<>();

        player.getLevel().getLoadedEntitiesOfClass(MCAVillager.class, null).stream().forEach(e -> villagers.add((MCAVillager) e));
        
        villagers.stream().filter(e -> e.isMarriedTo(player.getUUID()) || e.playerIsParent(player)).forEach(e -> {
        	
        	CompoundNBT nbt = new CompoundNBT();
            e.addAdditionalSaveData(nbt);
            familyData.add(nbt);
            
        });
        
        return new GetFamilyResponse(familyData);
		
	}
	
	protected static void processMessage(CallToPlayer msg, ServerPlayerEntity player) {
        
        Optional<MCAVillager> entity = player.getLevel().getLoadedEntitiesOfClass(MCAVillager.class, null).stream().filter(e -> e.getUUID().equals(msg.getTargetUUID())).findFirst();
        
        entity.ifPresent(e -> {
        	
            e.setPos(player.getX(), player.getY(), player.getZ());
            //((LivingEntity)e).getNavigator().clearPath();
            
        });
        
        return;
		
	}
	
	protected static void processMessage(SetTexture msg, ServerPlayerEntity player) {
		
        Optional<MCAVillager> entity = player.getLevel().getLoadedEntitiesOfClass(MCAVillager.class, null).stream().filter(e -> e.getUUID().equals(msg.getTargetUUID())).findFirst();
        if(entity.isPresent()) entity.get().set(MCAVillager.TEXTURE, msg.getTexture());
        return;
		
	}
	
	protected static void processMessage(SetProfession msg, ServerPlayerEntity player) {

        boolean isCareerSet = false;
        Optional<VillagerEntity> entity = player.getLevel().getLoadedEntitiesOfClass(VillagerEntity.class, null).stream().filter(e -> e.getUUID().equals(msg.getTargetUUID())).findFirst();
        
        if (entity.isPresent()) {
        	
            // Loop through all professions in the registry
            for (Entry<RegistryKey<VillagerProfession>, VillagerProfession> professionEntry : ForgeRegistries.PROFESSIONS.getEntries() ) {
            	
                List<VillagerProfession> careers = ObfuscationReflectionHelper.getPrivateValue(VillagerProfession.class, professionEntry.getValue(), "3");

                // Career ids are based on their index in the careers list
                for (int i = 0; i < careers.size(); i++) {
                	
                    VillagerProfession career = careers.get(i);

                    // If we found the correct career, set the profession and career accordingly
                    if (career.getRegistryName().getPath().equals(msg.getProfession())) {
                    	
                        MCAVillager villager = (MCAVillager)entity.get();
                        villager.getVillagerData().setProfession(professionEntry.getValue());
                        villager.setVanillaCareer(Integer.toString(i));
                        player.sendMessage(new StringTextComponent("Career set to " + msg.getProfession()), player.getUUID());
                        isCareerSet = true;
                        break;
                        
                    }
                    
                }
                
            }
            
        } else {
        	
            MCA.getLog().error("Entity not found on career set!: " + msg.getTargetUUID().toString());
            return;
            
        }

        if (!isCareerSet) {
        	
            player.sendMessage(new StringTextComponent("Career not found: " + msg.getProfession()), player.getUUID());
            
        }
        
        return;
		
	}
	
}
