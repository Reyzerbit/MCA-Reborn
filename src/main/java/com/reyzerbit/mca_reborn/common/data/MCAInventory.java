package com.reyzerbit.mca_reborn.common.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.reyzerbit.mca_reborn.common.entities.MCAVillager;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class MCAInventory extends Inventory {
	
    private MCAVillager villager;

    public MCAInventory(MCAVillager villager) {
    	
        super(27);
        
        this.villager = villager;
        
    }

    public int getFirstSlotContainingItem(Item item) {
    	
        for (int i = 0; i < this.getContainerSize(); i++) {
        	
            ItemStack stack = this.getItem(i);
            if (stack.getItem() != item) continue;
            return i;
            
        }
        
        return -1;
    }

    @SuppressWarnings("rawtypes")
	public boolean contains(Class clazz) {
    	
        for (int i = 0; i < this.getContainerSize(); ++i) {
        	
            final ItemStack stack = this.getItem(i);
            final Item item = stack.getItem();

            if (item.getClass() == clazz) return true;
            
        }
        
        return false;
        
    }

    /**
     * Gets the best quality (max damage) item of the specified type that is in the inventory.
     *
     * @param type The class of item that will be returned.
     * @return The item stack containing the item of the specified type with the highest max damage.
     */
    @SuppressWarnings("rawtypes")
	public ItemStack getBestItemOfType(@Nullable Class type) {
    	
        if (type == null) return ItemStack.EMPTY;
        else return getItem(getBestItemOfTypeSlot(type));
        
    }

    public ItemStack getBestArmorOfType(EquipmentSlotType slot) {
    	
        ItemStack returnStack = ItemStack.EMPTY;

        List<ItemStack> armors = new ArrayList<ItemStack>();
        for (int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack stack = this.getItem(i);
            if (stack.getItem() instanceof ArmorItem) {
            	ArmorItem armor = (ArmorItem) stack.getItem();
                if (armor.getSlot() == slot) armors.add(stack);
            }
        }

        int highestMaxDamage = 0;
        for (ItemStack stack : armors) {
            if (stack.getMaxDamage() > highestMaxDamage) {
                returnStack = stack;
                highestMaxDamage = stack.getMaxDamage();
            }
        }
        return returnStack;
        
    }

    @SuppressWarnings("rawtypes")
	public int getBestItemOfTypeSlot(Class type) {
        int highestMaxDamage = 0;
        int best = -1;

        for (int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack stackInInventory = this.getItem(i);

            final String itemClassName = stackInInventory.getItem().getClass().getName();

            if (itemClassName.equals(type.getName()) && highestMaxDamage < stackInInventory.getMaxDamage()) {
                highestMaxDamage = stackInInventory.getMaxDamage();
                best = i;
            }
        }

        return best;
    }

    public void dropAllItems() {
    	
        for (int i = 0; i < this.getContainerSize(); i++) {
        	
            ItemStack stack = this.getItem(i);
            ItemEntity stackEntity = new ItemEntity(null, villager.getX(), villager.getY(), villager.getZ(), stack);
            //TODO Spawn stackEntity
            
        }
        
    }

    public void readInventoryFromNBT(CompoundNBT tagList) {
    	
        for (int i = 0; i < this.getContainerSize(); ++i) {
        	
            this.setItem(i, ItemStack.EMPTY);
            
        }

        for (String nbtName : tagList.getAllKeys()) {
        	
        	CompoundNBT nbt = tagList.getCompound(nbtName);
            int slot = nbt.getByte("Slot") & 255;

            if (slot < this.getContainerSize()) {
            	
                this.setItem(slot, ItemStack.of(nbt));
                
            }
            
        }
        
    }
    

    public ListNBT writeInventoryToNBT() {
    	
    	ListNBT tagList = new ListNBT();

        for (int i = 0; i < this.getContainerSize(); ++i) {
        	
            ItemStack itemstack = this.getItem(i);

            if (itemstack != ItemStack.EMPTY) {
            	
            	CompoundNBT nbt = new CompoundNBT();
                nbt.putByte("Slot", (byte) i);
                itemstack.save(nbt);
                tagList.addTag(tagList.size(), nbt);
                
            }
            
        }

        return tagList;
        
    }

}