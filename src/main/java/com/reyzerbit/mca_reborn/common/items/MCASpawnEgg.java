package com.reyzerbit.mca_reborn.common.items;

import java.util.Optional;

import com.reyzerbit.mca_reborn.common.entities.MCAVillager;
import com.reyzerbit.mca_reborn.common.enums.EnumGender;
import com.reyzerbit.mca_reborn.common.init.EntityInit;

import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

public class MCASpawnEgg extends Item {
	
    private boolean isMale;

    public MCASpawnEgg(Properties prop, boolean isMale) {

        super(prop);
        this.isMale = isMale;
        
    }

    // PlayerEntity player, World world, BlockPos pos, Hand hand, EnumFacing side, float hitX, float hitY, float hitZ
    @Override
    public ActionResultType useOn(ItemUseContext use) {
    	
    	BlockPos pos = use.getClickedPos();
    	World world = use.getLevel();
    	PlayerEntity player = use.getPlayer();
    	
        int posX = pos.getX();
        int posY = pos.getY() + 1;
        int posZ = pos.getZ();

        if (!world.isClientSide) {
        	
            MCAVillager villager = new MCAVillager(EntityInit.MCA_VILLAGER.get(), world, Optional.of(isMale ? EnumGender.MALE : EnumGender.FEMALE));
            villager.setPos(posX + 0.5D, posY, posZ + 0.5D);
            // TODO Keep an eye on this, might be a little nasty
            villager.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(villager.blockPosition()), SpawnReason.SPAWN_EGG, null, null);
            world.addFreshEntity(villager);

            if (!player.isCreative()) player.inventory.setItem(player.inventory.selected, ItemStack.EMPTY);
            
        }

        return ActionResultType.PASS;
        
    }
    
}