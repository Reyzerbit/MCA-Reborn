package com.reyzerbit.mca_reborn.common;

import java.util.Date;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.reyzerbit.mca_reborn.api.API;
import com.reyzerbit.mca_reborn.client.render.RenderVillagerMCA;
import com.reyzerbit.mca_reborn.common.enums.EnumGender;
import com.reyzerbit.mca_reborn.common.init.BlockInit;
import com.reyzerbit.mca_reborn.common.init.EntityInit;
import com.reyzerbit.mca_reborn.common.init.ItemInit;
import com.reyzerbit.mca_reborn.common.util.EventHooks;
import com.reyzerbit.mca_reborn.common.util.Localizer;
import com.reyzerbit.mca_reborn.common.util.MCAConfig;
import com.reyzerbit.mca_reborn.common.world.OreGeneration;
import com.reyzerbit.mca_reborn.network.Network;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(MCA.MODID)
public class MCA {
	
    public static final String MODID = "mca_reborn";
    public static final String NAME = "Minecraft Comes Alive REBORN";
    public static final String VERSION = "1.0.1";
    
    //public static ServerProxy proxy;
    public static ItemGroup creativeTab;
    
    private static MCA instance;
    private static Logger logger;
    private static Localizer localizer;
    private static long startupTimestamp;
    private static IEventBus modBus;
    private static IEventBus forgeBus;
    public static String latestVersion = "";
    public static boolean updateAvailable = false;
    public String[] supporters = new String[0];
    
    public MCA() {
    	
    	ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MCAConfig.config);
    	
    	initMCAItemGroup();

    	modBus = FMLJavaModLoadingContext.get().getModEventBus();
    	forgeBus = MinecraftForge.EVENT_BUS;

    	modBus.register(this);
    	//bus.register(new EventHooks());
    	
    	modBus.addListener(this::commonSetup);
    	modBus.addListener(this::clientSetup);
    	modBus.addListener(this::serverSetup);
    	
    	modBus.addListener(Network::setupNetwork);
    	modBus.addListener(EventHooks::attributes);
    	
    	forgeBus.addListener(EventPriority.HIGH, OreGeneration::generateOres);
        
        ItemInit.ITEMS.register(modBus);
        BlockInit.BLOCKS.register(modBus);
        EntityInit.ENTITIES.register(modBus);
    	
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
    	
        startupTimestamp = new Date().getTime();
        instance = this;
        logger = LogManager.getLogger(MCA.MODID);
        localizer = new Localizer();
        
        //ProfessionsMCA.registerVillagerTypes();

        //proxy.registerEntityRenderers();
        //proxy.registerModelMeshers();
        
        API.init();
        
        //TODO Complete GUI
        //NetworkRegistry.registerGui(new GuiHandler());
        
        //TODO Update checking
        /*if (MCA.getConfig().allowUpdateChecking) {
            latestVersion = Util.httpGet("https://minecraftcomesalive.com/api/latest");
            if (!latestVersion.equals(VERSION) && !latestVersion.equals("")) {
                updateAvailable = true;
                MCA.getLog().warn("An update for Minecraft Comes Alive is available: v" + latestVersion);
            }
        }*/

        //TODO Supporters
        /*supporters = Util.httpGet("https://minecraftcomesalive.com/api/supporters").split(",");
        MCA.getLog().info("Loaded " + supporters.length + " supporters.");*/
        
    }
    
    public void clientSetup(final FMLClientSetupEvent event) {
    	
    	RenderingRegistry.registerEntityRenderingHandler(EntityInit.MCA_VILLAGER.get(), RenderVillagerMCA::new);
    	
    }
    
    public void serverSetup(final FMLDedicatedServerSetupEvent event) {
    	
    	//event.registerServerCommand(new CommandMCA());
        //event.registerServerCommand(new CommandAdminMCA());
    	
    }
    
    public void serverStopping(FMLServerStoppingEvent event) {}

    public String getRandomSupporter() {
    	
        if (supporters.length > 0) {
        	
            return supporters[new Random().nextInt(supporters.length)];
            
        } else {
        	
            return API.getRandomName(EnumGender.getRandom());
            
        }
     
    }

    public static Logger getLog() {
    	
        return logger;
        
    }

    public static MCA getInstance() {
    	
        return instance;
        
    }

    public static Localizer getLocalizer() {
    	
        return localizer;
        
    }

    public static long getStartupTimestamp() {
    	
        return startupTimestamp;
        
    }

	private void initMCAItemGroup() {
		
		MCAConfig.loadConfig(MCAConfig.config, FMLPaths.CONFIGDIR.get().resolve("mca-config.toml").toString());
        
        creativeTab = new ItemGroup("MCA") {

			@Override
			public ItemStack makeIcon() {
				
				return new ItemStack(ItemInit.GOLD_ENGAGEMENT_RING.get());
				
			}
            
        };
        
	}

	//TODO Check for crash reports
	public void checkForCrashReports() {

		
		
	}
    
}
