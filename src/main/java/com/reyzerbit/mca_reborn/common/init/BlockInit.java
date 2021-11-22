package com.reyzerbit.mca_reborn.common.init;

import com.reyzerbit.mca_reborn.common.MCA;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
	
	//REGISTER
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MCA.MODID);
	
	//BLOCKS
	public static final RegistryObject<Block> ROSE_GOLD_ORE = register("rose_gold_ore", new OreBlock(AbstractBlock.Properties.of(Material.STONE)
			.requiresCorrectToolForDrops()
			.strength(3.0F, 5.0F)
			.harvestTool(ToolType.PICKAXE)
			.harvestLevel(2)));
	public static final RegistryObject<Block> ROSE_GOLD_BLOCK = register("rose_gold_block", new Block(AbstractBlock.Properties.of(Material.STONE)
			.requiresCorrectToolForDrops()
			.strength(3.0F, 5.0F)
			.harvestTool(ToolType.PICKAXE)
			.harvestLevel(2)));
	public static final RegistryObject<Block> VILLAGER_SPAWNER = register("mca_villager_spawner", new Block(AbstractBlock.Properties.of(Material.STONE)
			.requiresCorrectToolForDrops()
			.strength(3.0F, 5.0F)
			.harvestTool(ToolType.PICKAXE)
			.harvestLevel(2)));
	
	
	
	private static void registerBlockItem(String name, RegistryObject<Block> block) {
		
		ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(),
				new Item.Properties().tab(MCA.creativeTab)));
		
	}
	
	
	
	private static RegistryObject<Block> register(String name, Block block) {
		
		RegistryObject<Block> toReturn = BLOCKS.register(name, () -> block);
		registerBlockItem(name, toReturn);
		return toReturn;
		
	}

}
