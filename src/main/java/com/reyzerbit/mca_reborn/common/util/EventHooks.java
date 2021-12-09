package com.reyzerbit.mca_reborn.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.reyzerbit.mca_reborn.client.gui.VillagerEditorScreen;
import com.reyzerbit.mca_reborn.client.gui.VillagerInteractionScreen;
import com.reyzerbit.mca_reborn.common.Constants;
import com.reyzerbit.mca_reborn.common.MCA;
import com.reyzerbit.mca_reborn.common.entities.MCAVillager;
import com.reyzerbit.mca_reborn.common.init.EntityInit;
import com.reyzerbit.mca_reborn.common.init.ItemInit;
import com.reyzerbit.mca_reborn.common.init.VillagerInit;
import com.reyzerbit.mca_reborn.common.items.Baby;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHooks {
	
    // Maps a player UUID to the itemstack of their held ItemBaby. Filled when a player dies so the baby is never lost.
    public static Map<UUID, ItemStack> limbo = new HashMap<>();
    
    // List of insults for when a baby is thrown
    private static String[] insultsDrop = {"You monster! I can't believe you'd throw a baby on the floor...",
    		"What a terrible parent! Throwing your baby on the floor like that...",
    		"Of all the terrible people in the world, you are the most terrible! Don't throw your baby on the floor!"};

    // Now handled in main MCA Class
    /*
    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> event) {
    	
        ItemsMCA.register(event);
        BlocksMCA.registerItemBlocks(event);

        GameRegistry.addSmelting(BlocksMCA.ROSE_GOLD_ORE, new ItemStack(ItemsMCA.ROSE_GOLD_INGOT), 5.0F);
        
    }
    */
    
    @SubscribeEvent
    public static void attributes(EntityAttributeCreationEvent event) {
    	
    	event.put(EntityInit.MCA_VILLAGER.get(), MCAVillager.createVillagerAttributes().build());
    	
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
    	
        if (!MCA.updateAvailable) return;
        StringTextComponent updateMessage = new StringTextComponent(Constants.Color.DARKGREEN + "An update for Minecraft Comes Alive is available: v" + MCA.latestVersion);
        String updateURLText = Constants.Color.YELLOW + "Click " + Constants.Color.BLUE + Constants.Format.ITALIC + Constants.Format.UNDERLINE + "here" + Constants.Format.RESET + Constants.Color.YELLOW + " to download the update.";

        StringTextComponent chatComponentUpdate = new StringTextComponent(updateURLText);
        chatComponentUpdate.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraftcomesalive.com/download"));
        chatComponentUpdate.getStyle().setUnderlined(true);

        event.getPlayer().sendMessage(updateMessage, event.getPlayer().getUUID());
        event.getPlayer().sendMessage(chatComponentUpdate, event.getPlayer().getUUID());

        MCA.updateAvailable = false;
        
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
    	
        // Only send crash reports on unloading the overworld. This will never change based on other mods installed
        // and ensures only one crash report is sent per instance.
        if (!event.getWorld().isClientSide() && event.getWorld().dimensionType().equals(DimensionType.OVERWORLD_LOCATION)) MCA.getInstance().checkForCrashReports();
        
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
    	
        World world = event.getWorld();
        Entity entity = event.getEntity();

        if (world.isClientSide()) return;
        if (!MCAConfig.overwriteOriginalVillagers.get()) return;

        if (entity.getClass().equals(VillagerEntity.class)) {
        	
        	VillagerEntity originalVillager = (VillagerEntity) entity;
            originalVillager.kill();

            MCAVillager newVillager = new MCAVillager(EntityInit.MCA_VILLAGER.get(), world, Optional.of(originalVillager.getVillagerData().getProfession()), Optional.empty());
            newVillager.setPos(originalVillager.position().x, originalVillager.position().y, originalVillager.position().z);
            newVillager.finalizeSpawn((IServerWorld) event.getWorld(), world.getCurrentDifficultyAt(newVillager.blockPosition()), SpawnReason.EVENT, null, null);
            newVillager.forcePositionAsHome();
            world.addFreshEntity(newVillager);
            
        }
        
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
    	
        ItemStack stack = event.getEntityItem().getItem();
        
        if (stack.getItem() instanceof Baby) {
        	
        	event.getPlayer().sendMessage(new StringTextComponent(insultsDrop[ThreadLocalRandom.current().nextInt(0, insultsDrop.length)]).withStyle(TextFormatting.RED), null);
        	event.getPlayer().inventory.placeItemBackInInventory(event.getPlayer().level, stack);
            event.setCanceled(true);
            
        }
        
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
    	
        // When players respawn check to see if their baby was saved in limbo. Add it back to their inventory.
        if (limbo.containsKey(event.getPlayer().getUUID())) {
        	
            event.getPlayer().inventory.add(limbo.get(event.getPlayer().getUUID()));
            limbo.remove(event.getPlayer().getUUID());
            
        }
        
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
    	
        // If a player dies while holding a baby, remember it until they respawn.
        if (event.getEntityLiving() instanceof PlayerEntity) {
        	
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            Optional<ItemStack> babyStack = player.inventory.items.stream().filter(s -> s.getItem() instanceof Baby).findFirst();
            babyStack.ifPresent(s -> limbo.put(player.getUUID(), babyStack.get()));
            
        }
        
    }

    @SubscribeEvent
    public static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
    	
        if (event.getTarget() instanceof MCAVillager && event.getPlayer() != null) {
        	
            PlayerEntity player = event.getPlayer();
            MCAVillager villager = (MCAVillager)event.getTarget();
            
            Minecraft.getInstance().setScreen(null);

            if (villager.getVillagerData().getProfession() == VillagerInit.BANDIT) {
            	
                event.setResult(Result.DENY);
                
            } else if (event.getWorld().isClientSide && event.getPlayer().getMainHandItem().getItem() == ItemInit.VILLAGER_EDITOR.get()) {
            	
            	Minecraft.getInstance().setScreen(new VillagerEditorScreen((MCAVillager) event.getTarget(), event.getPlayer()));
            	event.setResult(Result.ALLOW);
                
            } else {
            	
                player.awardStat(Stats.TALKED_TO_VILLAGER);
                Minecraft.getInstance().setScreen(new VillagerInteractionScreen(event.getEntity().getName(), (MCAVillager) event.getTarget(), event.getPlayer()));
                event.setResult(Result.ALLOW);
                
            }
            
        }
        
    }

    //TODO All this
    /*
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
    	
        if (event.getWorld().isRemote) event.getWorld().addEventListener(new WorldEventListenerMCA());
        
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
    	
        MCAServer.get().tick();
        
    }

    @SubscribeEvent
    public void onEntityDamaged(LivingDamageEvent event) {
        if (event.getEntity() instanceof EntityVillagerMCA) {
            EntityVillagerMCA villager = (EntityVillagerMCA)event.getEntity();
            Entity source = event.getSource() != null ? event.getSource().getTrueSource() : null;

            if (source instanceof EntityLivingBase) {
                villager.world.loadedEntityList.stream().filter(e ->
                        e instanceof EntityVillagerMCA &&
                        e.getDistance(villager) <= 10.0D &&
                        ((EntityVillagerMCA)e).getProfessionForge() == VillagerInit.guard)
                .forEach(e -> ((EntityVillagerMCA) e).setAttackTarget((EntityLivingBase)source));
            }
        }
    }

    @SubscribeEvent
    public void onPlaceEvent(BlockEvent.PlaceEvent event) {
        int x = event.getPos().getX();
        int y = event.getPos().getY();
        int z = event.getPos().getZ();
        Block placedBlock = event.getPlacedBlock().getBlock();

        if (placedBlock == Blocks.FIRE && event.getWorld().getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.EMERALD_BLOCK) {
            int totemsFound = 0;

            // Check on +/- X and Z for at least 3 totems on fire.
            for (int i = 0; i < 4; i++) {
                int dX = 0;
                int dZ = 0;

                if (i == 0 || i == 2) dX = -3;
                else dZ = 3;

                // Scan upwards to ensure it's obsidian, and on fire.
                for (int j = -1; j < 2; j++) {
                    Block block = event.getWorld().getBlockState(new BlockPos(x + dX, y + j, z + dZ)).getBlock();
                    if (block != Blocks.OBSIDIAN && block != Blocks.FIRE) break;

                    // If we made it up to 1 without breaking, make sure the block is fire so that it's a lit totem.
                    if (j == 1 && block == Blocks.FIRE) totemsFound++;
                }
            }

            if (totemsFound >= 3 && !event.getWorld().isDaytime()) {
                MCAServer.get().setReaperSpawnPos(event.getWorld(), new BlockPos(x + 1, y + 10, z + 1));
                MCAServer.get().startSpawnReaper();
                for (int i = 0; i < 2; i++) event.getWorld().setBlockToAir(new BlockPos(x, y - i, z));
            }
        }
    }

    @SubscribeEvent
    public void onLivingSetTarget(LivingSetAttackTargetEvent event) {
        // Mobs shouldn't attack infected villagers. Account for this when they attempt to set their target.
        if (event.getEntityLiving() instanceof EntityMob && event.getTarget() instanceof EntityVillagerMCA) {
            EntityMob mob = (EntityMob) event.getEntityLiving();
            EntityVillagerMCA target = (EntityVillagerMCA) event.getTarget();

            if (target.get(EntityVillagerMCA.IS_INFECTED)) {
                mob.setAttackTarget(null);
            }
        }
    }

    @SubscribeEvent
    public void onPlaySoundAtEntityEvent(PlaySoundAtEntityEvent event) {
        // Cancel all villager sounds. We unfortunately cannot control on a per entity basis as getEntity() always returns null.
        if (FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT) return;
        try {
            event.setCanceled(event.getSound().getSoundName().toString().contains("villager"));
        } catch (NullPointerException e) {
            // throw out potential NPEs due to bad event data. some of these have been reported
        }
    }
    */
}
