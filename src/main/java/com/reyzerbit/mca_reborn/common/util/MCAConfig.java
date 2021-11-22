package com.reyzerbit.mca_reborn.common.util;

import java.io.File;
import java.io.Serializable;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class MCAConfig implements Serializable {
	
	private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec config;
	
	private static final String PREFIX = "mca_reborn.";
	
	static {
		
		MCAConfig.initConfig(builder);
		config = builder.build();
		
	}
	
	public static void loadConfig(ForgeConfigSpec config, String path) {
		
		final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
		file.load();
		config.setConfig(file);
		
	}
    
	private static final long serialVersionUID = 6995938912938137646L;

	//private transient final ModConfig configOLD;

    public static ForgeConfigSpec.BooleanValue overwriteOriginalVillagers;
    public static ForgeConfigSpec.BooleanValue enableDiminishingReturns;
    public static ForgeConfigSpec.BooleanValue enableInfection;
    public static ForgeConfigSpec.IntValue infectionChance;
    public static ForgeConfigSpec.BooleanValue allowGrimReaper;
    public static ForgeConfigSpec.IntValue guardSpawnRate;
    public static ForgeConfigSpec.IntValue chanceToHaveTwins;
    public static ForgeConfigSpec.IntValue marriageHeartsRequirement;
    public static ForgeConfigSpec.IntValue roseGoldSpawnWeight;
    public static ForgeConfigSpec.IntValue babyGrowUpTime;
    public static ForgeConfigSpec.IntValue childGrowUpTime;
    public static ForgeConfigSpec.IntValue villagerSpawnerCap;
    public static ForgeConfigSpec.IntValue villagerSpawnerRateMinutes;
    public static ForgeConfigSpec.IntValue villagerMaxHealth;
    public static ForgeConfigSpec.BooleanValue allowTrading;
    public static ForgeConfigSpec.BooleanValue logVillagerDeaths;
    public static ForgeConfigSpec.BooleanValue enableRevivals;
    public static ForgeConfigSpec.ConfigValue<String> villagerChatPrefix;
    public static ForgeConfigSpec.BooleanValue allowPlayerMarriage;
    public static ForgeConfigSpec.BooleanValue enableAdminCommands;
    public static ForgeConfigSpec.BooleanValue allowCrashReporting;
    public static ForgeConfigSpec.BooleanValue allowUpdateChecking;
    public static ForgeConfigSpec.BooleanValue allowRoseGoldGeneration;

    /*
    public Config(final FMLCommonSetupEvent event) {
    	
        config = new ModConfig(event. .getSuggestedConfigurationFile());
        addConfigValues();
        
    }
    */
    
    public static void initConfig(ForgeConfigSpec.Builder config) {
    	
    	config.comment("MCA Reborn Config File");

        overwriteOriginalVillagers = config.comment("Should original villagers be overwritten by MCA villagers?")
        		.define(PREFIX + "villager_overwride", true);
        enableDiminishingReturns = config.comment("Should interactions yield diminishing returns over time?")
        		.define(PREFIX + "diminishing_returns", true);
        enableInfection = config.comment("Should zombies be able to infect villagers?")
        		.define(PREFIX + "infection_allowed", true);
        infectionChance = config.comment("Chance that a villager will be infected on hit from a zombie. Default is 5 for 5%.")
        		.defineInRange(PREFIX + "infection_chance", 5, 0, 100);
        allowGrimReaper = config.comment("Should the Grim Reaper boss be enabled?")
        		.define(PREFIX + "reaper_enabled", true);
        guardSpawnRate = config.comment("Chance that a villager will be infected on hit from a zombie. Default is 5 for 5%.")
        		.defineInRange(PREFIX + "guard_spawn_rate", 6, 0, 30);
        chanceToHaveTwins = config.comment("Chance that you will have twins. Default is 2 for 2%.")
        		.defineInRange(PREFIX + "twins_chance", 2, 0, 100);
        marriageHeartsRequirement = config.comment("Number of hearts required to get married.")
        		.defineInRange(PREFIX + "marriage_heart_count", 80, 50, 160);
        roseGoldSpawnWeight = config.comment("Spawn weights for Rose Gold.")
        		.defineInRange(PREFIX + "rose_gold_weight", 12, 0, 80);
        babyGrowUpTime = config.comment("Minutes it takes for a baby to be ready to grow up.")
        		.defineInRange(PREFIX + "baby_growth_time", 30, 10, 300);
        childGrowUpTime = config.comment("Minutes it takes for a child to grow into an adult.")
        		.defineInRange(PREFIX + "child_growth_time", 60, 10, 900);
        villagerSpawnerCap = config.comment("Maximum number of villagers that a spawner will create in the area before it stops.")
        		.defineInRange(PREFIX + "villager_spawn_cap", 5, 1, 30);
        villagerSpawnerRateMinutes = config.comment("The spawner will spawn 1 villager per this many minutes.")
        		.defineInRange(PREFIX + "villager_spawn_rate", 30, 1, 300);
        allowTrading = config.comment("Is trading with villagers enabled?")
        		.define(PREFIX + "trading_enabled", true);
        logVillagerDeaths = config.comment("Should villager deaths be logged?")
        		.define(PREFIX + "log_villager_deaths", true);
        enableRevivals = config.comment("Should reviving dead villagers be enabled?")
        		.define(PREFIX + "revivals_enabled", true);
        villagerChatPrefix = config.comment("Formatting prefix used for all chat with villagers.")
        		.define(PREFIX + "formatting_prefix", "");
        allowPlayerMarriage = config.comment("Enables or disables player marriage.")
        		.define(PREFIX + "player_marriage", true);
        enableAdminCommands = config.comment("Enables or disables MCA admin commands for ops.")
        		.define(PREFIX + "admin_commands", true);
        allowCrashReporting = config.comment("If enabled, sends crash reports to MCA developers.")
        		.define(PREFIX + "autosend_crash_reports", true);
        allowUpdateChecking = config.comment("If enabled, notifies you when an update to MCA is available.")
        		.define(PREFIX + "check_for_updates", true);
        allowRoseGoldGeneration = config.comment("If enabled, generates rose gold in your world. If disabled, generates stone instead.")
        		.define(PREFIX + "rose_gold_gen_enabled", true);
        villagerMaxHealth = config.comment("Each villager's maximum health. 1 point equals 1 heart.")
        		.defineInRange(PREFIX + "villager_max_health", 20, 5, 60);
        
    }

    /*
    private void addConfigValues() {
    	
        //overwriteOriginalVillagers = config.get("General", "Overwrite Original Villagers?", true, "Should original villagers be overwritten by MCA villagers?").getBoolean();
        //enableDiminishingReturns = config.get("General", "Enable Interaction Fatigue?", true, "Should interactions yield diminishing returns over time?").getBoolean();
        //enableInfection = config.get("General", "Enable Zombie Infection?", true, "Should zombies be able to infect villagers?").getBoolean();
        //infectionChance = config.get("General", "Chance of Infection", 5, "Chance that a villager will be infected on hit from a zombie. Default is 5 for 5%.").getInt();
        //allowGrimReaper = config.get("General", "Allow Grim Reaper?", true, "Should the Grim Reaper boss be enabled?").getBoolean();
        //guardSpawnRate = config.get("General", "Guard Spawn Rate", 6, "How many villagers that should be in a village before a guard spawns.").getInt();
        //chanceToHaveTwins = config.get("General", "Chance to Have Twins", 2, "Chance that you will have twins. Default is 2 for 2%.").getInt();
        //marriageHeartsRequirement = config.get("General", "Marriage Hearts Requirement", 100, "Number of hearts required to get married.").getInt();
        //roseGoldSpawnWeight = config.get("General", "Rose Gold Spawn Weight", 6, "Spawn weights for Rose Gold").getInt();
        //babyGrowUpTime = config.get("General", "Baby Grow Up Time (Minutes)", 30, "Minutes it takes for a baby to be ready to grow up.").getInt();
        //childGrowUpTime = config.get("General", "Child Grow Up Time (Minutes)", 60, "Minutes it takes for a child to grow into an adult.").getInt();
        //villagerSpawnerCap = config.get("General", "Villager Spawner Cap", 5, "Maximum number of villagers that a spawner will create in the area before it stops.").getInt();
        //villagerSpawnerRateMinutes = config.get("General", "Villager Spawner Rate", 30, "The spawner will spawn 1 villager per this many minutes.").getInt();
        //allowTrading = config.get("General", "Enable Trading?", true, "Is trading with villagers enabled?").getBoolean();
        //logVillagerDeaths = config.get("General", "Log Villager Deaths?", true, "Should villager deaths be logged?").getBoolean();
        //enableRevivals = config.get("General", "Enable Revivals?", true, "Should reviving dead villagers be enabled?").getBoolean();
        //villagerChatPrefix = config.get("General", "Villager Chat Prefix", "", "Formatting prefix used for all chat with villagers.").getString();
        //allowPlayerMarriage = config.get("General", "Allow Player Marriage?", true, "Enables or disables player marriage.").getBoolean();
        //enableAdminCommands = config.get("General", "Enable Admin Commands?", true, "Enables or disables MCA admin commands for ops.").getBoolean();
        //allowCrashReporting = config.get("General", "Allow Crash Reporting?", true, "If enabled, sends crash reports to MCA developers.").getBoolean();
        //allowUpdateChecking = config.get("General", "Allow Update Checking?", true, "If enabled, notifies you when an update to MCA is available.").getBoolean();
        //allowRoseGoldGeneration = config.get("General", "Allow Rose Gold World Generation", true, "If enabled, generates rose gold in your world. If disabled, generates stone instead.").getBoolean();
        //villagerMaxHealth = config.get("General", "Villager Max Health", 20, "Each villager's maximum health. 1 point equals 1 heart.").getInt();
        //config.save();
        
    }
    */

    /*
    public Configuration getInstance() {
    	
        return config;
        
    }

    public List<IConfigElement> getCategories() {
    	
        List<IConfigElement> elements = new ArrayList<>();

        for (String s : config.getCategoryNames()) {
        	
            if (s.equals("server")) continue;

            IConfigElement element = new ConfigElement(config.getCategory(s));
            elements.addAll(element.getChildElements());
            
        }

        return elements;
        
    }
    */
    
}