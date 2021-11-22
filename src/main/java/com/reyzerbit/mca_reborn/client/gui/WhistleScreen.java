package com.reyzerbit.mca_reborn.client.gui;

import static com.reyzerbit.mca_reborn.common.entities.MCAVillager.AGE_STATE;
import static com.reyzerbit.mca_reborn.common.entities.MCAVillager.GIRTH;
import static com.reyzerbit.mca_reborn.common.entities.MCAVillager.IS_INFECTED;
import static com.reyzerbit.mca_reborn.common.entities.MCAVillager.TALLNESS;
import static com.reyzerbit.mca_reborn.common.entities.MCAVillager.TEXTURE;
import static com.reyzerbit.mca_reborn.common.entities.MCAVillager.VILLAGER_NAME;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.reyzerbit.mca_reborn.common.MCA;
import com.reyzerbit.mca_reborn.common.entities.MCAVillager;
import com.reyzerbit.mca_reborn.network.MCAMessages;
import com.reyzerbit.mca_reborn.network.Network;

import lombok.NonNull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WhistleScreen extends Screen {
	
    public WhistleScreen() {
    	
		super(new StringTextComponent("Whistle GUI"));
	}

	private MCAVillager dummyHuman;
    private List<CompoundNBT> villagerDataList;

    private Button selectionLeftButton;
    private Button selectionRightButton;
    private Button villagerNameButton;
    private Button callButton;
    private Button exitButton;
    private int loadingAnimationTicks;
    private int selectedIndex;

    @Override
    public void tick() {
    	
        super.tick();

        if (loadingAnimationTicks != -1) {
        	
            loadingAnimationTicks++;
            
        }

        if (loadingAnimationTicks >= 20) {
        	
            loadingAnimationTicks = 0;
            
        }
        
    }

    @Override
    public void init() {
    	
    	selectionLeftButton = new Button(width / 2 - 123, height / 2 + 65, 20, 20, new StringTextComponent("<<"), button -> {

    		if(!dataExists()) return;
    		
            if (selectedIndex == 1) {
            	
                selectedIndex = villagerDataList.size();
                
            }

            else {
            	
                selectedIndex--;
                
            }
            
            updateVillagerName();
    		
    	});
    	
    	selectionRightButton = new Button(width / 2 + 103, height / 2 + 65, 20, 20, new StringTextComponent(">>"), button -> {

    		if(!dataExists()) return;
    		
            if (selectedIndex == villagerDataList.size()) {
            	
                selectedIndex = 1;
                
            }

            else {
            	
                selectedIndex++;
                
            }
            
            updateVillagerName();
    		
    	});
    	
    	villagerNameButton = new Button(width / 2 - 100, height / 2 + 65, 200, 20, null, null);
    	
    	callButton = new Button(width / 2 - 100, height / 2 + 90, 60, 20, new StringTextComponent(MCA.getLocalizer().localize("gui.button.call")), button -> {

    		if(!dataExists()) return;

            Network.INSTANCE.sendToServer(new MCAMessages.CallToPlayer(villagerDataList.get(selectedIndex - 1).getUUID("uuid")));
            this.onClose();
            updateVillagerName();
    		
    	});
    	
    	exitButton = new Button(width / 2 + 40, height / 2 + 90, 60, 20, new StringTextComponent(MCA.getLocalizer().localize("gui.button.exit")), button -> this.onClose());
    	
        buttons.clear();
        buttons.add(selectionLeftButton);
        buttons.add(selectionRightButton);
        buttons.add(villagerNameButton);
        buttons.add(callButton);
        buttons.add(exitButton);
        
        Network.INSTANCE.sendToServer(new MCAMessages.GetFamily());
        
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    
    private void updateVillagerName() {

		if(!dataExists()) return;

        villagerNameButton.setMessage(new StringTextComponent(villagerDataList.get(selectedIndex - 1).getString("name")));
        updateDummyVillagerWithData(villagerDataList.get(selectedIndex - 1));
        
    }
    
    private boolean dataExists() {
    	
    	return (villagerDataList != null && villagerDataList.size() > 0);
    	
    }
    

    @Override
    public void render(MatrixStack matrix, int sizeX, int sizeY, float offset) {
    	
        renderBackground(matrix);
        
        drawCenteredString(matrix, font, MCA.getLocalizer().localize("gui.title.whistle"), width / 2, height / 2 - 110, 0xffffff);

        if (loadingAnimationTicks != -1) {
        	
            drawString(matrix, font, "Loading" + StringUtils.repeat(".", loadingAnimationTicks % 10), width / 2 - 20, height / 2 - 10, 0xffffff);
            
        }

        else {
        	
            if (villagerDataList.size() == 0) {
            	
                drawCenteredString(matrix, font, "No family members could be found in the area.", width / 2, height / 2 + 50, 0xffffff);
                
            }

            else {
            	
                drawCenteredString(matrix, font, selectedIndex + " / " + villagerDataList.size(), width / 2, height / 2 + 50, 0xffffff);
                
            }
            
        }

        if (dummyHuman != null) {
        	
            drawDummyVillager();
            
        }

        super.render(matrix, sizeX, sizeY, offset);
        
    }

    private void drawDummyVillager() {
    	
        final int posX = width / 2;
        int posY = height / 2 + 45;
        InventoryScreen.renderEntityInInventory(posX, posY, 75, 0F, 0F, (LivingEntity) dummyHuman);
        
    }

    public void setVillagerDataList(@NonNull List<CompoundNBT> dataList) {
    	
        this.villagerDataList = dataList;
        this.loadingAnimationTicks = -1;
        this.selectedIndex = 1;

        try {
        	
        	CompoundNBT firstData = dataList.get(0);
        	villagerNameButton.setMessage(new StringTextComponent(firstData.getString("name")));
        	
            dummyHuman = new MCAVillager(EntityType.VILLAGER, getPlayerClient().level);
            updateDummyVillagerWithData(firstData);
            
        }

        catch (IndexOutOfBoundsException e) {
        	
            callButton.active = false;
            
        }
        
    }

    @OnlyIn(Dist.CLIENT)
    private static PlayerEntity getPlayerClient() {
    	
    	Minecraft mc = Minecraft.getInstance();
        return mc.player;

    }

    private void updateDummyVillagerWithData(CompoundNBT nbt) {
    	
        dummyHuman.set(VILLAGER_NAME, nbt.getString("name"));
        dummyHuman.set(TEXTURE, nbt.getString("texture"));
        dummyHuman.set(GIRTH, nbt.getFloat("girth"));
        dummyHuman.set(TALLNESS, nbt.getFloat("tallness"));
        dummyHuman.set(IS_INFECTED, nbt.getBoolean("infected"));
        dummyHuman.set(AGE_STATE, nbt.getInt("ageState"));
        
    }
}