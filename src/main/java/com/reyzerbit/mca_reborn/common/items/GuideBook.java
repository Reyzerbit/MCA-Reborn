package com.reyzerbit.mca_reborn.common.items;

import com.reyzerbit.mca_reborn.common.init.ItemInit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.client.gui.screen.ReadBookScreen.WrittenBookInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.World;

public class GuideBook extends WrittenBookItem {

    public GuideBook(Properties prop) {
    	
        super(prop);
        
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int unknownInt, boolean unknownBoolean) {
    	
        super.inventoryTick(itemStack, world, entity, unknownInt, unknownBoolean);

        if(!makeSureTagIsValid(itemStack.getTag())) {
        	
        	ItemInit.setBookNBT(itemStack);
        	resolveBookComponents(itemStack, entity.createCommandSourceStack(), (PlayerEntity) entity);
        	
        }
        
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
    	
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        
        if (worldIn.isClientSide) {
        	
        	Minecraft.getInstance().setScreen(new ReadBookScreen(new WrittenBookInfo(itemstack)));
            
        }
        
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemstack);
        
    }

    public static void resolveBookContents(ItemStack stack, PlayerEntity player) {
    	
        if (stack.getTag() != null) {
        	
            CompoundNBT nbttagcompound = stack.getTag();

            if (!nbttagcompound.getBoolean("resolved")) {
            	
                nbttagcompound.putBoolean("resolved", true);

                if (makeSureTagIsValid(nbttagcompound)) {
                	
                    ListNBT nbttaglist = nbttagcompound.getList("pages", 8);

                    for (int i = 0; i < nbttaglist.size(); ++i) {
                    	
                        String s = nbttaglist.getString(i);
                        ITextComponent itextcomponent;

                        try {
                        	
                            itextcomponent = ITextComponent.Serializer.fromJsonLenient(s);
                            //itextcomponent = TextComponentUtils.processComponent(player, itextcomponent, player);
                            itextcomponent = TextComponentUtils.updateForEntity(player.createCommandSourceStack(), itextcomponent, player, i);
                            
                        } catch (Exception var9) {
                        	
                            itextcomponent = new StringTextComponent(s);
                            
                        }
                        
                        nbttaglist.set(i, StringNBT.valueOf(ITextComponent.Serializer.toJson(itextcomponent)));
                        
                    }
                    
                    nbttagcompound.put("pages", nbttaglist);

                    if (player instanceof ServerPlayerEntity && player.getMainHandItem() == stack) {
                    	
                        Slot slot = player.containerMenu.getSlot(player.inventory.selected);
                        ((ServerPlayerEntity) player).connection.send(new SSetSlotPacket(0, slot.index, stack));
                        
                    }
                    
                }
                
            }
            
        }
        
    }
    
}