package com.reyzerbit.mca_reborn.common.init;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableSet;
import com.reyzerbit.mca_reborn.common.MCA;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MCA.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VillagerInit {
	
	public static final VillagerProfession GUARD = new VillagerProfession("guard", PointOfInterestType.UNEMPLOYED, ImmutableSet.of(), ImmutableSet.of(), null).setRegistryName(MCA.MODID + "guard");
    public static final VillagerProfession BANDIT = new VillagerProfession("bandit", PointOfInterestType.UNEMPLOYED, ImmutableSet.of(), ImmutableSet.of(), null).setRegistryName(MCA.MODID + "bandit");
    public static final VillagerProfession CHILD = new VillagerProfession("child", PointOfInterestType.UNEMPLOYED, ImmutableSet.of(), ImmutableSet.of(), null).setRegistryName(MCA.MODID + "child");
    public static final VillagerProfession BAKER = new VillagerProfession("baker", PointOfInterestType.UNEMPLOYED, ImmutableSet.of(), ImmutableSet.of(), null).setRegistryName(MCA.MODID + "baker");
    public static final VillagerProfession MINER = new VillagerProfession("miner", PointOfInterestType.UNEMPLOYED, ImmutableSet.of(), ImmutableSet.of(), null).setRegistryName(MCA.MODID + "miner");
    
    public static final List<VillagerProfession> MCA_PROFESSIONS = Arrays.asList(GUARD, BANDIT, CHILD, BAKER, MINER);
    private static Map<ResourceLocation, VillagerProfession> PROFESSIONS_MAP = new HashMap<ResourceLocation, VillagerProfession>();
    
    private static final VillagerProfession[] FORBIDDEN_RANDOM_PROFESSIONS = { BANDIT, CHILD };
    
    //Values from original VillagerTrades.java
    final float emeraldForItemsMultiplier = 0.05F;
    final float itemForEmeraldMultiplier = 0.05F;
    final float rareItemForEmeraldMultiplier = 0.2F;
	
	@SubscribeEvent
    public static void registerVillagerProfessions(final RegistryEvent.Register<VillagerProfession> event) {

        event.getRegistry().register(GUARD);
        event.getRegistry().register(BANDIT);
        event.getRegistry().register(CHILD);
        event.getRegistry().register(BAKER);
        event.getRegistry().register(MINER);
        
        for(ResourceLocation r : event.getRegistry().getKeys()){
        	
        	PROFESSIONS_MAP.put(r, event.getRegistry().getValue(r));
        	
        }
        
    }
	
	public static void addTrades(Int2ObjectMap<List<VillagerTrades.ITrade>> trades, VillagerProfession type) {
		
		if(type.equals(GUARD)) {
			
			
			
		} else if(type.equals(CHILD)) {
			
			
			
		} else if(type.equals(BAKER)) {
			
			
			
		} else if(type.equals(BANDIT)) {
			
			
			
		} else if(type.equals(MINER)) {
			
			
			
		}
		
	}

    public static VillagerProfession randomProfession() {
    	
        ResourceLocation resource = null;
        
        while (resource == null || resource.getPath().contains("nitwit") || inForbiddenProfessions(PROFESSIONS_MAP.get(resource))) {
        	
            resource = (ResourceLocation) PROFESSIONS_MAP.keySet().toArray()[new Random().nextInt(PROFESSIONS_MAP.size())];
            
        }
        
        return PROFESSIONS_MAP.get(resource);
        
    }

    private static boolean inForbiddenProfessions(VillagerProfession profIn) {
    	
        for (VillagerProfession profession : FORBIDDEN_RANDOM_PROFESSIONS) {
        	
            if (profession == profIn) {
            	
                return true;
                
            }
            
        }
        
        return false;
        
    }
	
	//TODO Improve villager interaction with blocks and sounds.
    /*

    public static VillagerType guard_warrior;
    public static VillagerType guard_archer;
    public static VillagerType guard_hero;
    public static VillagerType bandit_marauder;
    public static VillagerType bandit_outlaw;
    public static VillagerType bandit_pillager;
    public static VillagerType child_child;
    public static VillagerType baker_baker;
    public static VillagerType miner_miner;

    public static void registerVillagerTypes() {
    	
        guard_warrior = new VillagerType("warrior");
        guard_archer = new VillagerType("archer");
        guard_hero = new VillagerType(guard, "hero");
        bandit_marauder = new VillagerType(bandit, "marauder");
        bandit_outlaw = new VillagerType(bandit, "outlaw");
        bandit_pillager = new VillagerType(bandit, "pillager");
        child_child = new VillagerType(child, "child");
        baker_baker = new VillagerType(baker, "baker");
        miner_miner = new VillagerType(miner, "miner");

        baker_baker.addTrade(1, new BakerTradesLvl1());
        baker_baker.addTrade(2, new BakerTradesLvl2());
        baker_baker.addTrade(3, new BakerTradesLvl3());

        miner_miner.addTrade(1, new MinerTradesLvl1());
        miner_miner.addTrade(2, new MinerTradesLvl2());
        miner_miner.addTrade(3, new MinerTradesLvl3());
    }

    public static ItemStack getDefaultHeldItem(VillagerProfession profession, VillagerCareer career) {
    	
        if (profession == ProfessionsMCA.guard) return career == ProfessionsMCA.guard_archer ? ItemStackCache.get(Items.BOW) : ItemStackCache.get(Items.IRON_SWORD);
        else if (profession == ProfessionsMCA.bandit) return ItemStackCache.get(Items.IRON_SWORD);
        return ItemStack.EMPTY;
        
    }*/

    //@Mod.EventBusSubscriber(modid = "mca")
    /*public static class RegistrationHandler {
        **
         * Register this mod's {@link VillagerProfession}s.
         *
         * @param event The event
         *
        @SubscribeEvent
        public static void onEvent(final RegistryEvent.Register<VillagerProfession> event) {
            registry = event.getRegistry();
            registry.register(guard);
            registry.register(bandit);
            registry.register(child);
            registry.register(baker);
            registry.register(miner);
        }
    }

    public static class BakerTradesLvl1 implements EntityVillager.ITradeList {

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 1), new ItemStack(Items.WHEAT, 6)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.MILK_BUCKET, 1), new ItemStack(Items.BREAD, 2)));
        }
    }

    public static class BakerTradesLvl2 implements EntityVillager.ITradeList {

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2), new ItemStack(Items.EGG, 12)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.SUGAR, 2), new ItemStack(Items.CAKE, 1)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.STONE_HOE, 1), new ItemStack(Items.WHEAT, 10)));
        }
    }

    public static class BakerTradesLvl3 implements EntityVillager.ITradeList {

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            recipeList.add(new MerchantRecipe(new ItemStack(Items.CAKE, 1), new ItemStack(Items.EMERALD, 5)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2), new ItemStack(Items.BREAD, 4)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 3), new ItemStack(Items.COOKIE, 6)));
            recipeList.add(new MerchantRecipe(new ItemStack(Blocks.PUMPKIN, 1), new ItemStack(Items.PUMPKIN_PIE, 1)));
        }
    }

    public static class MinerTradesLvl1 implements EntityVillager.ITradeList {

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            recipeList.add(new MerchantRecipe(new ItemStack(Blocks.COBBLESTONE, 8), new ItemStack(Blocks.STONE, 4)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 1), new ItemStack(Blocks.TORCH, 8)));
        }
    }

    public static class MinerTradesLvl2 implements EntityVillager.ITradeList {

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            recipeList.add(new MerchantRecipe(new ItemStack(Items.GOLD_INGOT, 3), new ItemStack(Items.EMERALD, 2)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 1), new ItemStack(Items.COAL, 3)));
        }
    }

    public static class MinerTradesLvl3 implements EntityVillager.ITradeList {

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 4), new ItemStack(Items.IRON_PICKAXE, 1)));
            recipeList.add(new MerchantRecipe(new ItemStack(Blocks.EMERALD_BLOCK, 1), new ItemStack(Items.DIAMOND_PICKAXE, 1)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.IRON_INGOT, 8), new ItemStack(Items.DIAMOND, 1)));
        }
    }*/
}
