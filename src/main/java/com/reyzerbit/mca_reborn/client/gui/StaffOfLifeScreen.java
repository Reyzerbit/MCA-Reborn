package com.reyzerbit.mca_reborn.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.reyzerbit.mca_reborn.common.MCA;
import com.reyzerbit.mca_reborn.common.entities.MCAVillager;
import com.reyzerbit.mca_reborn.network.MCAMessages;
import com.reyzerbit.mca_reborn.network.Network;

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

public class StaffOfLifeScreen extends Screen {
	
    private Map<String, CompoundNBT> villagerData;
    private Button reviveButton;
    private Button nameButton;
    private Button backButton;
    private Button nextButton;
    private Button closeButton;
    private MCAVillager dummy;
    private PlayerEntity player;

    // selection fields
    private int index = 0;
    private List<String> keys = new ArrayList<>();

    @OnlyIn(Dist.CLIENT)
    public StaffOfLifeScreen(PlayerEntity player) {
    	
        super(new StringTextComponent("Staff of Life"));
        this.player = player;
        
    }

    @Override
    public void init(Minecraft minecraft, int w, int h) {
    	
    	reviveButton = new Button(width / 2 - 123, height / 2 + 65, 20, 20, new StringTextComponent(MCA.getLocalizer().localize("gui.button.revive")), button -> {
        	
            Network.INSTANCE.sendToServer(new MCAMessages.ReviveVillager(UUID.fromString(keys.get(index))));
            this.onClose();
            
        });
    	
    	nameButton = new Button(width / 2 - 100, height / 2 + 65, 200, 20, new StringTextComponent(""), null);
    	
    	backButton = new Button(width / 2 - 123, height / 2 + 65, 20, 20, new StringTextComponent("<<"), button -> selectData(index - 1));
    	
    	nextButton = new Button(width / 2 + 103, height / 2 + 65, 20, 20, new StringTextComponent(">>"), button -> selectData(index + 1));
    	
    	closeButton = new Button(width / 2 + 40, height / 2 + 90, 60, 20, new StringTextComponent(MCA.getLocalizer().localize("gui.button.exit")), button -> this.onClose());
    	
    	this.minecraft = minecraft;
    	width = w;
    	height = h;
    	
        Network.INSTANCE.sendToServer(new MCAMessages.SavedVillagersRequest());

        buttons.clear();
        buttons.add(backButton);
        buttons.add(nextButton);
        buttons.add(nameButton);
        buttons.add(reviveButton);
        buttons.add(closeButton);
        
    }

    @Override
    public boolean isPauseScreen() {
    	
        return false;
        
    }

    @Override
    public void render(MatrixStack matrix, int sizeX, int sizeY, float offset) {
    	
        renderBackground(matrix);
        drawDummy();
        drawCenteredString(matrix, font, MCA.getLocalizer().localize("gui.title.staffoflife"), width / 2, height / 2 - 110, 0xffffff);
        super.render(matrix, sizeX, sizeY, offset);
        
    }

    public void setVillagerData(Map<String, CompoundNBT> data) {
    	
        villagerData = data;

        if (data.size() > 0) {
        	
            dummy = new MCAVillager(EntityType.VILLAGER, player.level);
            keys.addAll(data.keySet());
            selectData(0);
            
        } else {
        	
            nameButton.setMessage(new StringTextComponent("No villagers found."));
            backButton.active = false;
            nextButton.active = false;
            nameButton.active = false;
            reviveButton.active = false;
            
        }
        
    }

    private void updateDummy(CompoundNBT nbt) {
    	
        dummy.deserializeNBT(nbt);
        dummy.setHealth(20.0F);
        
    }

    private void selectData(int i) {
    	
        if (i < 0) i = keys.size() - 1;
        else if (i > keys.size() - 1) i = 0;

        index = i;
        updateDummy(villagerData.get(keys.get(index)));
        nameButton.setMessage(dummy.getDisplayName());
        
    }

    private void drawDummy() {
    	
        int posX = width / 2;
        int posY = height / 2 + 45;
        
        if (dummy != null) InventoryScreen.renderEntityInInventory(posX, posY, 60, 0F, 0F, (LivingEntity) dummy);
        
    }
}