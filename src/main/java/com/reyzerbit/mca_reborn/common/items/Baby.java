package com.reyzerbit.mca_reborn.common.items;

import java.util.List;
import java.util.Optional;

import com.reyzerbit.mca_reborn.api.API;
import com.reyzerbit.mca_reborn.client.gui.NameBabyScreen;
import com.reyzerbit.mca_reborn.common.Constants;
import com.reyzerbit.mca_reborn.common.MCA;
import com.reyzerbit.mca_reborn.common.data.ParentData;
import com.reyzerbit.mca_reborn.common.data.PlayerData;
import com.reyzerbit.mca_reborn.common.data.PlayerSaveData;
import com.reyzerbit.mca_reborn.common.entities.MCAVillager;
import com.reyzerbit.mca_reborn.common.enums.EnumAgeState;
import com.reyzerbit.mca_reborn.common.enums.EnumDialogueType;
import com.reyzerbit.mca_reborn.common.enums.EnumGender;
import com.reyzerbit.mca_reborn.common.init.VillagerInit;
import com.reyzerbit.mca_reborn.common.util.Localizer;
import com.reyzerbit.mca_reborn.common.util.MCAConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class Baby extends Item {
	
    private boolean isMale;
	
	public Baby(Properties prop, boolean isMale) {
		
		super(prop);
		
		this.isMale = isMale;
		// STack Size is now handled in the deferred register
		
	}

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int unknownInt, boolean unknownBoolean) {
    	
        super.inventoryTick(itemStack, world, entity, unknownInt, unknownBoolean);

        if (!world.isClientSide) {
        	
            if (!itemStack.hasTag()) {
            	
                CompoundNBT compound = new CompoundNBT();

                compound.putString("name", "");
                compound.putInt("age", 0);
                compound.putUUID("ownerUUID", entity.getUUID());
                compound.putString("ownerName", entity.getName().getString());
                compound.putBoolean("isInfected", false);

                itemStack.setTag(compound);
                
            } else {
            	
                updateBabyGrowth(itemStack);
                
            }
            
        }
        
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
    	
        BlockPos pos = player.blockPosition();
        ItemStack stack = player.getItemInHand(hand);
        int posX = pos.getX();
        int posY = pos.getY() + 1;
        int posZ = pos.getZ();

        // Right-clicking an unnamed baby allows you to name it
        if (world.isClientSide && getBabyName(stack).equals(""))
        	
        	Minecraft.getInstance().setScreen(new NameBabyScreen(this.getDefaultInstance()));
            //GuiWrapper.openGui(MCA.getInstance(), Constants.GUI_ID_NAMEBABY, player.level, player.getId(), 0, 0);

        if (!world.isClientSide) {
        	
            if (isReadyToGrowUp(stack) && !getBabyName(stack).equals("")) { // Name is good and we're ready to grow
            	
            	MCAVillager child = new MCAVillager(EntityType.VILLAGER, world, Optional.of(VillagerInit.CHILD), Optional.of(this.isMale ? EnumGender.MALE : EnumGender.FEMALE));
            	
                child.set(MCAVillager.VILLAGER_NAME, getBabyName(stack));
                child.set(MCAVillager.TEXTURE, API.getRandomSkin(child)); // allow for special-case skins to be applied with the proper name attached to the child at this point
                child.set(MCAVillager.AGE_STATE, EnumAgeState.BABY.getId());
                child.setStartingAge(MCAConfig.childGrowUpTime.get() * 60 * 20 * -1);
                child.setBaby(true);
                child.setPos(posX, posY, posZ);
                world.addFreshEntity(child);

                PlayerSaveData playerSaveData = ((ServerWorld) world).getChunkSource().getDataStorage().computeIfAbsent(PlayerSaveData::new, PlayerSaveData.SAVED_DATA_NAME);
                
                Optional<PlayerData> playerData = playerSaveData.get(player.getUUID());
                
                if(playerData.isPresent()) {
                	
                	PlayerData data = playerData.get();
                    
                    child.set(MCAVillager.PARENTS, ParentData.create(player.getUUID(), data.getSpouseUUID(), player.getName().getString(), data.getSpouseName()).toNBT());
                    player.inventory.setItem(player.inventory.selected, ItemStack.EMPTY);
                    data.setBabyPresent(false);

                    // set proper dialogue type
                    child.getPlayerHistoryFor(player.getUUID()).setDialogueType(EnumDialogueType.CHILDP);
                	
                }
                
            }
            
        }

        return super.use(world, player, hand);
        
    }

    private String getBabyName(ItemStack stack) {
        return stack.getTag().getString("name");
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entityItem) {
    	
        if (!entityItem.level.isClientSide) {
        	
            updateBabyGrowth(entityItem.getItem());
            
        }

        return super.onEntityItemUpdate(null, entityItem);
        
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    	
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        Localizer loc = MCA.getLocalizer();

        if (stack.hasTag()) {
        	
        	Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            CompoundNBT nbt = stack.getTag();
            String textColor = ((Baby) stack.getItem()).isMale ? Constants.Color.AQUA : Constants.Color.LIGHTPURPLE;
            int ageInMinutes = nbt.getInt("age");
            String ownerName = nbt.getUUID("ownerUUID").equals(player.getUUID()) ? MCA.getLocalizer().localize("label.you") : nbt.getString("ownerName");

            if (getBabyName(stack).equals(""))
                tooltip.add(new StringTextComponent(textColor + loc.localize("gui.label.name") + " " + Constants.Format.RESET + MCA.getLocalizer().localize("label.unnamed")));
            else
                tooltip.add(new StringTextComponent(textColor + loc.localize("gui.label.name") + " " + Constants.Format.RESET + nbt.getString("name")));

            tooltip.add(new StringTextComponent(textColor + loc.localize("gui.label.age") + " " + Constants.Format.RESET + ageInMinutes
            		+ " " + (ageInMinutes == 1 ? loc.localize("gui.label.minute") : loc.localize("gui.label.minutes"))));
            tooltip.add(new StringTextComponent(textColor + loc.localize("gui.label.parent") + " " + Constants.Format.RESET + ownerName));

            if (nbt.getBoolean("isInfected")) tooltip.add(new StringTextComponent(Constants.Color.GREEN + loc.localize("gui.label.infected")));
            if (isReadyToGrowUp(stack)) tooltip.add(new StringTextComponent(Constants.Color.GREEN + loc.localize("gui.label.readytogrow")));
            if (nbt.getString("name").equals(loc.localize("gui.label.unnamed"))) tooltip.add(new StringTextComponent(Constants.Color.YELLOW + loc.localize("gui.label.rightclicktoname")));
        }
    }

    private void updateBabyGrowth(ItemStack itemStack) {
    	
        if (itemStack.hasTag() && ServerLifecycleHooks.getCurrentServer().getTickCount() % 1200 == 0) {
        	
            int age = itemStack.getTag().getInt("age");
            age++;
            itemStack.getTag().putInt("age", age);
            
        }
        
    }

    private boolean isReadyToGrowUp(ItemStack itemStack) {
    	
        return itemStack.getTag().getInt("age") >= MCAConfig.babyGrowUpTime.get();
        
    }

    public EnumGender getGender() {
    	
        return isMale ? EnumGender.MALE : EnumGender.FEMALE;
        
    }
}