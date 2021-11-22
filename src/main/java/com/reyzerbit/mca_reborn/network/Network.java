package com.reyzerbit.mca_reborn.network;

import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_SERVER;

import java.util.Optional;

import com.reyzerbit.mca_reborn.common.MCA;
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
import com.reyzerbit.mca_reborn.network.MCAMessages.Say;
import com.reyzerbit.mca_reborn.network.MCAMessages.SetName;
import com.reyzerbit.mca_reborn.network.MCAMessages.SetProfession;
import com.reyzerbit.mca_reborn.network.MCAMessages.SetTexture;
import com.reyzerbit.mca_reborn.network.MCAMessages.SpawnParticles;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Network {

    private static int NETWORK_INDEX = 0;
	
	private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE;
    static {
    	
    	INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MCA.MODID, "mca_network_channel"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    	
    }
    
    @SubscribeEvent
    public static void setupNetwork(FMLCommonSetupEvent event) {
    	
    	// Beneath each message register is the original code for backwards reference

    	// Client -> Server
    	INSTANCE.registerMessage(NETWORK_INDEX++, ButtonAction.class, ButtonAction::encode, ButtonAction::decode, MessageHandler.Server::handle, Optional.of(PLAY_TO_SERVER));
        //INSTANCE.registerMessage(0, ButtonActionHandler.class, ButtonAction.class, Side.SERVER);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, BabyName.class, BabyName::encode, BabyName::decode, MessageHandler.Server::handle, Optional.of(PLAY_TO_SERVER));
        //INSTANCE.registerMessage(BabyNameHandler.class, BabyName.class, 2, Side.SERVER);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, CareerRequest.class, CareerRequest::encode, CareerRequest::decode, MessageHandler.Server::handle, Optional.of(PLAY_TO_SERVER));
        //INSTANCE.registerMessage(CareerRequestHandler.class, CareerRequest.class, 4, Side.SERVER);
    	
    	INSTANCE.registerMessage(NETWORK_INDEX++, InventoryRequest.class, InventoryRequest::encode, InventoryRequest::decode, MessageHandler.Server::handle, Optional.of(PLAY_TO_SERVER));
        //INSTANCE.registerMessage(InventoryRequestHandler.class, InventoryRequest.class, 5, Side.SERVER);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, SavedVillagersRequest.class, SavedVillagersRequest::encode, SavedVillagersRequest::decode, MessageHandler.Server::handle, Optional.of(PLAY_TO_SERVER));
        //INSTANCE.registerMessage(SavedVillagersRequestHandler.class, SavedVillagersRequest.class, 7, Side.SERVER);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, ReviveVillager.class, ReviveVillager::encode, ReviveVillager::decode, MessageHandler.Server::handle, Optional.of(PLAY_TO_SERVER));
        //INSTANCE.registerMessage(ReviveVillagerHandler.class, ReviveVillager.class, 9, Side.SERVER);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, SetName.class, SetName::encode, SetName::decode, MessageHandler.Server::handle, Optional.of(PLAY_TO_SERVER));
        //INSTANCE.registerMessage(SetNameHandler.class, SetName.class, 10, Side.SERVER);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, GetFamily.class, GetFamily::encode, GetFamily::decode, MessageHandler.Server::handle, Optional.of(PLAY_TO_SERVER));
        //INSTANCE.registerMessage(GetFamilyHandler.class, GetFamily.class, 12, Side.SERVER);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, CallToPlayer.class, CallToPlayer::encode, CallToPlayer::decode, MessageHandler.Server::handle, Optional.of(PLAY_TO_SERVER));
        //INSTANCE.registerMessage(CallToPlayerHandler.class, CallToPlayer.class, 14, Side.SERVER);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, SetTexture.class, SetTexture::encode, SetTexture::decode, MessageHandler.Server::handle, Optional.of(PLAY_TO_SERVER));
        //INSTANCE.registerMessage(SetTextureHandler.class, SetTexture.class, 15, Side.SERVER);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, SetProfession.class, SetProfession::encode, SetProfession::decode, MessageHandler.Server::handle, Optional.of(PLAY_TO_SERVER));
        //INSTANCE.registerMessage(SetProfessionHandler.class, SetProfession.class, 16, Side.SERVER);
        
        // Server -> Client
    	INSTANCE.registerMessage(NETWORK_INDEX++, SpawnParticles.class, SpawnParticles::encode, SpawnParticles::decode, MessageHandler.Client::handle, Optional.of(PLAY_TO_CLIENT));
        //INSTANCE.registerMessage(SpawnParticlesHandler.class, SpawnParticles.class, 11, Side.CLIENT);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, Say.class, Say::encode, Say::decode, MessageHandler.Client::handle, Optional.of(PLAY_TO_CLIENT));
        //INSTANCE.registerMessage(SayHandler.class, Say.class, 1, Side.CLIENT);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, CareerResponse.class, CareerResponse::encode, CareerResponse::decode, MessageHandler.Client::handle, Optional.of(PLAY_TO_CLIENT));
        //INSTANCE.registerMessage(CareerResponseHandler.class, CareerResponse.class, 3, Side.CLIENT);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, InventoryResponse.class, InventoryResponse::encode, InventoryResponse::decode, MessageHandler.Client::handle, Optional.of(PLAY_TO_CLIENT));
        //INSTANCE.registerMessage(InventoryResponseHandler.class, InventoryResponse.class, 6, Side.CLIENT);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, SavedVillagersResponse.class, SavedVillagersResponse::encode, SavedVillagersResponse::decode, MessageHandler.Client::handle, Optional.of(PLAY_TO_CLIENT));
        //INSTANCE.registerMessage(SavedVillagersResponseHandler.class, SavedVillagersResponse.class, 8, Side.CLIENT);
        
    	INSTANCE.registerMessage(NETWORK_INDEX++, GetFamilyResponse.class, GetFamilyResponse::encode, GetFamilyResponse::decode, MessageHandler.Client::handle, Optional.of(PLAY_TO_CLIENT));
        //INSTANCE.registerMessage(GetFamilyResponseHandler.class, GetFamilyResponse.class, 13, Side.CLIENT);
        
    }

    // TRANSFER COMPLETE
    /*
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ButtonAction implements IMessage {
        private String guiKey;
        private String buttonId;
        private UUID targetUUID;

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeBoolean(targetUUID != null);
            ByteBufUtils.writeUTF8String(buf, this.guiKey);
            ByteBufUtils.writeUTF8String(buf, this.buttonId);

            if (targetUUID != null) {
                ByteBufUtils.writeUTF8String(buf, this.targetUUID.toString());
            }
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            boolean hasTarget = buf.readBoolean();
            this.guiKey = ByteBufUtils.readUTF8String(buf);
            this.buttonId = ByteBufUtils.readUTF8String(buf);

            if (hasTarget) {
                this.targetUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
            }
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class ButtonActionHandler implements IMessageHandler<ButtonAction, IMessage> {
        @Override
        public IMessage onMessage(ButtonAction message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;

            // The message can target a particular villager, or the server itself.
            if (!message.targetsServer()) {
                EntityVillagerMCA villager = (EntityVillagerMCA) player.getServerWorld().getEntityFromUuid(message.targetUUID);
                if (villager != null) player.getServerWorld().addScheduledTask(() -> villager.handleButtonClick(player, message.guiKey, message.buttonId));
            } else ServerMessageHandler.handleMessage(player, message);
            return null;
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Say implements IMessage {
        private String phraseId;
        private int speakingEntityId;

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, this.phraseId);
            buf.writeInt(this.speakingEntityId);
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.phraseId = ByteBufUtils.readUTF8String(buf);
            this.speakingEntityId = buf.readInt();
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class SayHandler implements IMessageHandler<Say, IMessage> {

        @Override
        public IMessage onMessage(Say message, MessageContext ctx) {
            EntityPlayer player = getPlayerClient();
            EntityVillagerMCA villager = (EntityVillagerMCA) player.getEntityWorld().getEntityByID(message.speakingEntityId);

            if (villager != null) villager.say(com.google.common.base.Optional.of(player), message.phraseId);

            return null;
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BabyName implements IMessage {
        private String babyName;

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, this.babyName);
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.babyName = ByteBufUtils.readUTF8String(buf);
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class BabyNameHandler implements IMessageHandler<BabyName, IMessage> {

        @Override
        public IMessage onMessage(BabyName message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            ItemStack stack = player.inventory.getStackInSlot(player.inventory.currentItem);

            if (stack.getItem() instanceof ItemBaby) stack.getTagCompound().setString("name", message.babyName);

            return null;
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CareerResponse implements IMessage {
        private int careerId;
        private UUID entityUUID;

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeVarInt(buf, careerId, 4);
            ByteBufUtils.writeUTF8String(buf, entityUUID.toString());
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.careerId = ByteBufUtils.readVarInt(buf, 4);
            this.entityUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class CareerResponseHandler implements IMessageHandler<CareerResponse, IMessage> {

        @Override
        public IMessage onMessage(CareerResponse message, MessageContext ctx) {
            // must be thrown in the queue and processed on the main thread since we must loop through the loaded entity list
            // it could change while looping and cause a ConcurrentModificationException.
            ClientMessageQueue.add(message);
            return null;
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CareerRequest implements IMessage {
        private UUID entityUUID;

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, entityUUID.toString());
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.entityUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class CareerRequestHandler implements IMessageHandler<CareerRequest, IMessage> {

        @Override
        public IMessage onMessage(CareerRequest message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            int careerId = -255;

            try {
                EntityVillagerMCA villager = (EntityVillagerMCA) player.getServerWorld().getEntityFromUuid(message.entityUUID);

                if (villager != null) careerId = ObfuscationReflectionHelper.getPrivateValue(EntityVillager.class, villager, EntityVillagerMCA.VANILLA_CAREER_ID_FIELD_INDEX);
            } catch (ClassCastException ignored) {
                MCA.getLog().error("UUID provided in career request does not match an MCA villager!: " + message.entityUUID.toString());
                return null;
            } catch (NullPointerException ignored) {
                MCA.getLog().error("UUID provided in career request does not match a loaded MCA villager!: " + message.entityUUID.toString());
                return null;
            }

            if (careerId == -255) {
                MCA.getLog().error("Career ID wasn't assigned for UUID: " + message.entityUUID);
                return null;
            }

            return new CareerResponse(careerId, message.entityUUID);
            
        }
        
    }
    */

    // TRANSFER COMPLETE
    /*
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InventoryRequest implements IMessage {
        private UUID entityUUID;

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, entityUUID.toString());
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.entityUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class InventoryRequestHandler implements IMessageHandler<InventoryRequest, IMessage> {

        @Override
        public IMessage onMessage(InventoryRequest message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            EntityVillagerMCA villager = (EntityVillagerMCA) player.getServerWorld().getEntityFromUuid(message.entityUUID);
            if (villager != null && villager.inventory != null) return new InventoryResponse(villager.getUniqueID(), villager.inventory);
            return null;
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @NoArgsConstructor
    @Getter
    public static class InventoryResponse implements IMessage {
        private UUID entityUUID;
        private NBTTagCompound inventoryNBT;

        public InventoryResponse(UUID entityUUID, MCAInventory inventory) {
            this.inventoryNBT = new NBTTagCompound();
            this.entityUUID = entityUUID;
            this.inventoryNBT.setTag("inventory", inventory.writeInventoryToNBT());
        }

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, entityUUID.toString());
            ByteBufUtils.writeTag(buf, inventoryNBT);
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.entityUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
            this.inventoryNBT = ByteBufUtils.readTag(buf);
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class InventoryResponseHandler implements IMessageHandler<InventoryResponse, IMessage> {

        @Override
        public IMessage onMessage(InventoryResponse message, MessageContext ctx) {
            ClientMessageQueue.add(message);
            return null;
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class SavedVillagersRequest implements IMessage {

        @Override
        public void fromBytes(ByteBuf buf) {}

        @Override
        public void toBytes(ByteBuf buf) {}
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class SavedVillagersRequestHandler implements IMessageHandler<SavedVillagersRequest, IMessage> {

        @Override
        public IMessage onMessage(SavedVillagersRequest message, MessageContext ctx) {
            return new SavedVillagersResponse(ctx.getServerHandler().player);
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @NoArgsConstructor
    public static class SavedVillagersResponse implements IMessage {
        private Map<String, NBTTagCompound> villagers = new HashMap<>();

        public SavedVillagersResponse(EntityPlayer player) {
            villagers = SavedVillagers.get(player.world).getMap();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(villagers.size());
            villagers.forEach((k,v) -> {
                ByteBufUtils.writeUTF8String(buf, k);
                ByteBufUtils.writeTag(buf, v);
            });
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                String k = ByteBufUtils.readUTF8String(buf);
                NBTTagCompound v = ByteBufUtils.readTag(buf);
                villagers.put(k, v);
            }
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class SavedVillagersResponseHandler implements IMessageHandler<SavedVillagersResponse, IMessage> {

        @Override
        public IMessage onMessage(SavedVillagersResponse message, MessageContext ctx) {
            GuiScreen screen = Minecraft.getMinecraft().currentScreen;
            if (screen instanceof GuiStaffOfLife) ((GuiStaffOfLife) screen).setVillagerData(message.villagers);
            return null;
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviveVillager implements IMessage {
        private UUID target;

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, target.toString());
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            target = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class ReviveVillagerHandler implements IMessageHandler<ReviveVillager, IMessage> {

        @Override
        public IMessage onMessage(ReviveVillager message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            SavedVillagers villagers = SavedVillagers.get(player.world);
            NBTTagCompound nbt = SavedVillagers.get(player.world).loadByUUID(message.target);
            if (nbt != null) {
                EntityVillagerMCA villager = new EntityVillagerMCA(player.world);
                villager.setPosition(player.posX, player.posY, player.posZ);
                player.world.spawnEntity(villager);

                villager.readEntityFromNBT(nbt);
                villager.reset();

                villagers.remove(message.target);
                player.inventory.mainInventory.get(player.inventory.currentItem).damageItem(1, player);
            }

            return null;
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SetName implements IMessage {
        private String name;
        private UUID entityUUID;

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, entityUUID.toString());
            ByteBufUtils.writeUTF8String(buf, name);
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            entityUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
            name = ByteBufUtils.readUTF8String(buf);
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class SetNameHandler implements IMessageHandler<SetName, IMessage> {

        @Override
        public IMessage onMessage(SetName message, MessageContext ctx) {
            World world = ctx.getServerHandler().player.world;
            java.util.Optional<Entity> entity = world.loadedEntityList.stream().filter((e) -> e.getUniqueID().equals(message.entityUUID)).findFirst();
            if (!entity.isPresent()) return null;
            if (entity.get() instanceof EntityVillagerMCA) {
                EntityVillagerMCA villager = (EntityVillagerMCA) entity.get();
                villager.set(EntityVillagerMCA.VILLAGER_NAME, message.name);
            }
            return null;
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpawnParticles implements IMessage {
        private UUID entityUUID;
        private EnumParticleTypes particleType;

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, entityUUID.toString());
            buf.writeInt(particleType.getParticleID());
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            entityUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
            particleType = EnumParticleTypes.getParticleFromId(buf.readInt());
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class SpawnParticlesHandler implements IMessageHandler<SpawnParticles, IMessage> {
        @Override
        public IMessage onMessage(SpawnParticles message, MessageContext ctx) {
            World world = getPlayerClient().world;
            java.util.Optional<Entity> entity = world.loadedEntityList.stream().filter((e) -> e.getUniqueID().equals(message.entityUUID)).findFirst();
            if (!entity.isPresent()) return null;
            if (entity.get() instanceof EntityVillagerMCA) {
                EntityVillagerMCA villager = (EntityVillagerMCA) entity.get();
                villager.spawnParticles(message.particleType);
            }
            return null;
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @NoArgsConstructor
    public static class GetFamily implements IMessage {
        @Override
        public void toBytes(ByteBuf buf) {}

        @Override
        public void fromBytes(ByteBuf buf) {}
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class GetFamilyHandler implements IMessageHandler<GetFamily, IMessage> {
        @Override
        public IMessage onMessage(GetFamily message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            List<EntityVillagerMCA> villagers = new ArrayList<>();
            List<NBTTagCompound> familyData = new ArrayList<>();

            player.world.loadedEntityList.stream().filter(e -> e instanceof EntityVillagerMCA).forEach(e -> villagers.add((EntityVillagerMCA)e));
            villagers.stream().filter(e -> e.isMarriedTo(player.getUniqueID()) || e.playerIsParent(player)).forEach(e -> {
                NBTTagCompound nbt = new NBTTagCompound();
                e.writeEntityToNBT(nbt);
                familyData.add(nbt);
            });
            return new GetFamilyResponse(familyData);
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetFamilyResponse implements IMessage {
        private List<CompoundNBT> familyData;

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(familyData.size());
            familyData.stream().forEach(n -> ByteBufUtils.writeTag(buf, n));
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            familyData = new ArrayList<>();
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                familyData.add(ByteBufUtils.readTag(buf));
            }
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class GetFamilyResponseHandler implements IMessageHandler<GetFamilyResponse, IMessage> {
        @Override
        public IMessage onMessage(GetFamilyResponse message, MessageContext ctx) {
            GuiScreen screen = Minecraft.getMinecraft().currentScreen;
            if (screen instanceof GuiWhistle) {
                GuiWhistle whistleScreen = (GuiWhistle)screen;
                whistleScreen.setVillagerDataList(message.familyData);
            }
            return null;
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CallToPlayer implements IMessage {
        private UUID targetUUID;

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, targetUUID.toString());
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            targetUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class CallToPlayerHandler implements IMessageHandler<CallToPlayer, IMessage> {
        @Override
        public IMessage onMessage(CallToPlayer message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            Optional<Entity> entity = player.world.loadedEntityList.stream().filter(e -> e.getUniqueID().equals(message.targetUUID)).findFirst();
            entity.ifPresent(e -> {
                e.setPosition(player.posX, player.posY, player.posZ);
                ((EntityLiving)e).getNavigator().clearPath();
            });
            return null;
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SetProfession implements IMessage {
        private UUID targetUUID;
        private String profession;

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, targetUUID.toString());
            ByteBufUtils.writeUTF8String(buf, profession);
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            targetUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
            profession = ByteBufUtils.readUTF8String(buf);
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class SetProfessionHandler implements IMessageHandler<SetProfession, IMessage> {
        @Override
        public IMessage onMessage(SetProfession message, MessageContext ctx) {
            boolean isCareerSet = false;
            EntityPlayer player = ctx.getServerHandler().player;
            Optional<Entity> entity = player.world.loadedEntityList.stream().filter(e -> e.getUniqueID().equals(message.targetUUID)).findFirst();
            if (entity.isPresent()) {
                // Loop through all professions in the registry
                for (Map.Entry<ResourceLocation, VillagerRegistry.VillagerProfession> professionEntry : VillagerInit.registry.getEntries()) {
                    List<VillagerRegistry.VillagerCareer> careers = ObfuscationReflectionHelper.getPrivateValue(VillagerRegistry.VillagerProfession.class, professionEntry.getValue(), 3);

                    // Career ids are based on their index in the careers list
                    for (int i = 0; i < careers.size(); i++) {
                        VillagerRegistry.VillagerCareer career = careers.get(i);

                        // If we found the correct career, set the profession and career accordingly
                        if (career.getName().equals(message.profession)) {
                            EntityVillagerMCA villager = (EntityVillagerMCA)entity.get();
                            villager.setProfession(professionEntry.getValue());
                            villager.setVanillaCareer(i);
                            player.sendMessage(new TextComponentString("Career set to " + message.profession));
                            isCareerSet = true;
                            break;
                        }
                    }
                }
            } else {
                MCA.getLog().error("Entity not found on career set!: " + message.targetUUID.toString());
                return null;
            }

            if (!isCareerSet) {
                player.sendMessage(new TextComponentString("Career not found: " + message.profession));
            }
            return null;
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SetTexture implements IMessage {
        private UUID targetUUID;
        private String texture;

        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, targetUUID.toString());
            ByteBufUtils.writeUTF8String(buf, texture);
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            targetUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
            texture = ByteBufUtils.readUTF8String(buf);
        }
    }
    */

    // TRANSFER COMPLETE
    /*
    public static class SetTextureHandler implements IMessageHandler<SetTexture, IMessage> {
        @Override
        public IMessage onMessage(SetTexture message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            Optional<Entity> entity = player.world.loadedEntityList.stream().filter(e -> e.getUniqueID().equals(message.targetUUID)).findFirst();
            entity.ifPresent(e -> ((EntityVillagerMCA)e).set(EntityVillagerMCA.TEXTURE, message.texture));
            return null;
        }
    }
    */
    
}