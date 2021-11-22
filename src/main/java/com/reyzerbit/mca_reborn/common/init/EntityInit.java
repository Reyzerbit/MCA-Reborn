package com.reyzerbit.mca_reborn.common.init;

import com.reyzerbit.mca_reborn.common.MCA;
import com.reyzerbit.mca_reborn.common.entities.MCAVillager;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit {
    
    //EntityRegistry.registerModEntity(new ResourceLocation(MODID, "EntityVillagerMCA"), EntityVillagerMCA.class, EntityVillagerMCA.class.getSimpleName(), 1120, this, 50, 2, true);
    //EntityRegistry.registerModEntity(new ResourceLocation(MODID, "GrimReaperMCA"), EntityGrimReaper.class, EntityGrimReaper.class.getSimpleName(), 1121, this, 50, 2, true);
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MCA.MODID);
	
	public static final RegistryObject<EntityType<MCAVillager>> MCA_VILLAGER = ENTITIES.register("mca_villager", 
			() -> EntityType.Builder.of(MCAVillager::create, EntityClassification.MISC).sized(0.8F, 1.9F).build(new ResourceLocation(MCA.MODID, "mca_villager").getNamespace()));

}
