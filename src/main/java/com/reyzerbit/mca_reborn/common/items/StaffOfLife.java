package com.reyzerbit.mca_reborn.common.items;

import java.util.List;

import javax.annotation.Nullable;

import com.reyzerbit.mca_reborn.client.gui.StaffOfLifeScreen;
import com.reyzerbit.mca_reborn.common.Constants;
import com.reyzerbit.mca_reborn.common.MCA;
import com.reyzerbit.mca_reborn.common.util.MCAConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StaffOfLife extends Item {
    public StaffOfLife(Properties prop) {
    	
        super(prop);
        
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
    	
        if (!MCAConfig.enableRevivals.get())
            playerIn.sendMessage(new StringTextComponent(MCA.getLocalizer().localize("notify.revival.disabled")), playerIn.getUUID());
        
        if(worldIn.isClientSide) {

            //playerIn.openGui(MCA.getInstance(), Constants.GUI_ID_STAFFOFLIFE, playerIn.level, 0, 0, 0);
        	Minecraft.getInstance().setScreen(new StaffOfLifeScreen(playerIn));
        	
        }
        
        return super.use(worldIn, playerIn, handIn);
        
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    	
        tooltip.add(new StringTextComponent("Uses left: " + (itemStack.getMaxDamage() - itemStack.getDamageValue() + 1)));
        
        if (Screen.hasShiftDown()) {
        	
            tooltip.add(new StringTextComponent("Use to revive a previously dead"));
            tooltip.add(new StringTextComponent("villager, but all of their memories"));
            tooltip.add(new StringTextComponent("will be forgotten."));
            
        } else tooltip.add(new StringTextComponent("Hold " + Constants.Color.YELLOW + "SHIFT" + Constants.Color.GRAY + " for info."));
        
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean isFoil(ItemStack itemStack) {
    	
        return true;
        
    }
    
}
