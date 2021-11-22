package com.reyzerbit.mca_reborn.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.reyzerbit.mca_reborn.common.data.MCAInventory;
import com.reyzerbit.mca_reborn.common.data.SavedVillagers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class MCAMessages {
	
	// for Item Stacks - ByteBufUtils.writeItemStack()
    // for NBT Tags ByteBufUtils.writeTag();
    // for Strings: ByteBufUtils.writeUTF8String();
	
	// Server to Client Messages
    @AllArgsConstructor
    @Getter
	public static class ButtonAction {
		
        private String guiKey;
        private String buttonId;
        private UUID targetUUID;
		
		public static ButtonAction decode(PacketBuffer buffer) {
			
			return new ButtonAction(buffer.readUtf(), buffer.readUtf(), buffer.readUUID());
			
		}
		
		public static void encode(ButtonAction msg, PacketBuffer buffer) {
			
			buffer.writeUtf(msg.getGuiKey());
			buffer.writeUtf(msg.getButtonId());
			buffer.writeUUID(msg.getTargetUUID());
			
		}

        public boolean targetsServer() {
        	
            return getTargetUUID() == null;
            
        }
		
	}

    @AllArgsConstructor
    @Getter
    public static class BabyName {
    	
    	private String babyName;
		
		public static BabyName decode(PacketBuffer buffer) {
			
			return new BabyName(buffer.readUtf());
			
		}
		
		public static void encode(BabyName msg, PacketBuffer buffer) {
			
			buffer.writeUtf(msg.getBabyName());
			
		}
    	
    }
    
    @AllArgsConstructor
    @Getter
    public static class CareerRequest {
    	
    	private UUID entityUUID;
		
		public static CareerRequest decode(PacketBuffer buffer) {
			
			return new CareerRequest(buffer.readUUID());
			
		}
		
		public static void encode(CareerRequest msg, PacketBuffer buffer) {
			
			buffer.writeUUID(msg.getEntityUUID());
			
		}
    	
    }
    
    @AllArgsConstructor
    @Getter
    public static class InventoryRequest {
    	
    	private UUID entityUUID;
		
		public static InventoryRequest decode(PacketBuffer buffer) {
			
			return new InventoryRequest(buffer.readUUID());
			
		}
		
		public static void encode(InventoryRequest msg, PacketBuffer buffer) {
			
			buffer.writeUUID(msg.getEntityUUID());
			
		}
    	
    }
    
    @AllArgsConstructor
    @Getter
    public static class SavedVillagersRequest {
		
		public static SavedVillagersRequest decode(PacketBuffer buffer) {
			
			return new SavedVillagersRequest();
			
		}
		
		public static void encode(SavedVillagersRequest msg, PacketBuffer buffer) {}
    	
    }
    
    @AllArgsConstructor
    @Getter
    public static class ReviveVillager {
    	
    	private UUID targetUUID;
		
		public static ReviveVillager decode(PacketBuffer buffer) {
			
			return new ReviveVillager(buffer.readUUID());
			
		}
		
		public static void encode(ReviveVillager msg, PacketBuffer buffer) {
			
			buffer.writeUUID(msg.getTargetUUID());
			
		}
    	
    }
    
    @AllArgsConstructor
    @Getter
    public static class SetName {
    	
        private String name;
        private UUID entityUUID;
		
		public static SetName decode(PacketBuffer buffer) {
			
			return new SetName(buffer.readUtf(), buffer.readUUID());
			
		}
		
		public static void encode(SetName msg, PacketBuffer buffer) {
			
			buffer.writeUtf(msg.getName());
			buffer.writeUUID(msg.getEntityUUID());
			
		}
        
    }
    
    @AllArgsConstructor
    @Getter
    public static class GetFamily {
		
		public static GetFamily decode(PacketBuffer buffer) {
			
			return new GetFamily();
			
		}
		
		public static void encode(GetFamily msg, PacketBuffer buffer) {}
    	
    }
    
    @AllArgsConstructor
    @Getter
    public static class CallToPlayer {
    	
    	private UUID targetUUID;
		
		public static CallToPlayer decode(PacketBuffer buffer) {
			
			return new CallToPlayer(buffer.readUUID());
			
		}
		
		public static void encode(CallToPlayer msg, PacketBuffer buffer) {
			
			buffer.writeUUID(msg.getTargetUUID());
			
		}
    	
    }
    
    @AllArgsConstructor
    @Getter
    public static class SetTexture {

        private String texture;
        private UUID targetUUID;
        
        public static SetTexture decode(PacketBuffer buffer) {
			
			return new SetTexture(buffer.readUtf(), buffer.readUUID());
			
		}
		
		public static void encode(SetTexture msg, PacketBuffer buffer) {
			
			buffer.writeUtf(msg.getTexture());
			buffer.writeUUID(msg.getTargetUUID());
			
		}
    	
    }
    
    @AllArgsConstructor
    @Getter
    public static class SetProfession {

        private String profession;
        private UUID targetUUID;
        
        public static SetProfession decode(PacketBuffer buffer) {
			
			return new SetProfession(buffer.readUtf(), buffer.readUUID());
			
		}
		
		public static void encode(SetProfession msg, PacketBuffer buffer) {
			
			buffer.writeUtf(msg.getProfession());
			buffer.writeUUID(msg.getTargetUUID());
			
		}
    	
    }
    
    //Client to Server Messages
    @AllArgsConstructor
    @Getter
    public static class SpawnParticles {
    	
        private UUID entityUUID;
        private BasicParticleType particleType;
        
        public void test() {
        	
        }
		
		public static SpawnParticles decode(PacketBuffer buffer) {
			
			return new SpawnParticles(buffer.readUUID(), (BasicParticleType) ForgeRegistries.PARTICLE_TYPES.getValue(ResourceLocation.tryParse(buffer.readUtf())));
			
		}
		
		public static void encode(SpawnParticles msg, PacketBuffer buffer) {
			
			buffer.writeUUID(msg.getEntityUUID());
			buffer.writeUtf(msg.getParticleType().getRegistryName().toString());
			
		}
    	
    }
    
    @AllArgsConstructor
    @Getter
    public static class Say {

        private String phraseId;
        private int speakingEntityId;
        
        public void test() {
        	
        }
		
		public static Say decode(PacketBuffer buffer) {
			
			return new Say(buffer.readUtf(), buffer.readInt());
			
		}
		
		public static void encode(Say msg, PacketBuffer buffer) {
			
			buffer.writeUtf(msg.getPhraseId());
			buffer.writeInt(msg.getSpeakingEntityId());
			
		}
    	
    }
    
    @AllArgsConstructor
    @Getter
    public static class CareerResponse {

        private int careerId;
        private UUID entityUUID;
		
		public static CareerResponse decode(PacketBuffer buffer) {
			
			return new CareerResponse(buffer.readInt(), buffer.readUUID());
			
		}
		
		public static void encode(CareerResponse msg, PacketBuffer buffer) {
			
			buffer.writeInt(msg.getCareerId());
			buffer.writeUUID(msg.getEntityUUID());
			
		}
    	
    }
    
    @Getter
    public static class InventoryResponse {

        private UUID entityUUID;
        private CompoundNBT inventoryNBT;

        public InventoryResponse(UUID entityUUID, MCAInventory inventory) {
        	
            this.inventoryNBT = new CompoundNBT();
            this.entityUUID = entityUUID;
            this.inventoryNBT.put("inventory", inventory.writeInventoryToNBT());
            
        }

        public InventoryResponse(UUID entityUUID, CompoundNBT inventory) {
        	
            this.inventoryNBT = inventory;
            this.entityUUID = entityUUID;
            
        }
		
		public static InventoryResponse decode(PacketBuffer buffer) {
			
			return new InventoryResponse(buffer.readUUID(), buffer.readNbt());
			
		}
		
		public static void encode(InventoryResponse msg, PacketBuffer buffer) {
			
			buffer.writeUUID(msg.getEntityUUID());
			buffer.writeNbt(msg.getInventoryNBT());
			
		}
    	
    }

    @Getter
    public static class SavedVillagersResponse {
    	
        private Map<String, CompoundNBT> villagers = new HashMap<>();

        public SavedVillagersResponse(ServerPlayerEntity player) {
        	
            villagers = player.getLevel().getDataStorage().computeIfAbsent(SavedVillagers::getData, "MCA-Villagers").getMap();
            
        }

        public SavedVillagersResponse(Map<String, CompoundNBT> map) {
        	
            villagers = map;
            
        }
		
		public static SavedVillagersResponse decode(PacketBuffer buffer) {
			
			Map<String, CompoundNBT> map = new HashMap<String, CompoundNBT>();
			
            int size = buffer.readInt();
            
            for (int i = 0; i < size; i++) {
            	
                map.put(buffer.readUtf(), buffer.readNbt());
                
            }
			
			return new SavedVillagersResponse(map);
			
		}
		
		public static void encode(SavedVillagersResponse msg, PacketBuffer buffer) {

            buffer.writeInt(msg.getVillagers().size());
            
            msg.getVillagers().forEach((k,v) -> {
            	
                buffer.writeUtf(k);
                buffer.writeNbt(v);
                
            });
			
		}
    	
    }
    
    @AllArgsConstructor
    @Getter
    public static class GetFamilyResponse {

    	private List<CompoundNBT> familyData;
		
		public static GetFamilyResponse decode(PacketBuffer buffer) {

            List<CompoundNBT> data = new ArrayList<CompoundNBT>();
            int size = buffer.readInt();
            
            for (int i = 0; i < size; i++) {
            	
            	data.add(buffer.readNbt());
            	
            }
            
			return new GetFamilyResponse(data);
			
		}
		
		public static void encode(GetFamilyResponse msg, PacketBuffer buffer) {
			
			buffer.writeInt(msg.getFamilyData().size());
			msg.getFamilyData().stream().forEach(n -> buffer.writeNbt(n));
			
		}
    	
    }
    
}
