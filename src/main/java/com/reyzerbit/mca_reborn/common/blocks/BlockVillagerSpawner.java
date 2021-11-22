package com.reyzerbit.mca_reborn.common.blocks;

import net.minecraft.block.Block;

public class BlockVillagerSpawner extends Block {

	public BlockVillagerSpawner(Properties p_i48440_1_) {
		super(p_i48440_1_);
		// TODO Auto-generated constructor stub
	}

	/*
    public BlockVillagerSpawner() {
    	
        super(Material.IRON);
        setTickRandomly(true);
        setBlockUnbreakable();
        
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        super.updateTick(world, pos, state, random);

        int nearbyVillagers = world.getEntitiesWithinAABB(EntityVillagerMCA.class, new AxisAlignedBB(pos).expand(32D, 32D, 32D)).size();
        if (nearbyVillagers < MCA.getConfig().villagerSpawnerCap) {
            int yMod = 0;

            // Start from the current point possible and count up until air is hit. This allows the spawner to
            // be placed anywhere below ground and still spawn a villager on a top level.
            while (pos.getY() + yMod < 256) {
                BlockPos current = pos.add(0, yMod, 0);
                BlockPos above = pos.add(0, yMod + 1, 0);

                if (world.isAirBlock(current) && world.isAirBlock(above)) {
                    EntityVillagerMCA villager = new EntityVillagerMCA(world, Optional.absent(), Optional.absent());
                    villager.setPosition(current.getX(), current.getY(), current.getZ());
                    world.spawnEntity(villager);
                    break;
                }

                yMod++;
            }
        }

        world.scheduleUpdate(pos, this, MCA.getConfig().villagerSpawnerRateMinutes * 72000);
    }
    */
}