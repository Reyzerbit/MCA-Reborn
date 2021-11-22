package com.reyzerbit.mca_reborn.common.items;

import java.util.List;

import com.reyzerbit.mca_reborn.client.gui.WhistleScreen;

import net.minecraft.client.Minecraft;
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

public class Whistle extends Item {
	
    public Whistle(Properties prop) {
    	
        super(prop);
        
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
    	
    	Minecraft.getInstance().setScreen(new WhistleScreen());
        //player.openGui(MCA.getInstance(), Constants.GUI_ID_WHISTLE, world, (int)player.getX(), (int)player.getY(), (int)player.getZ());
        return super.use(world, player, hand);
        
    }

    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    	
        tooltip.add(new StringTextComponent("Allows you to call your family to your current location."));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        
    }
    
}