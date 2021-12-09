package com.reyzerbit.mca_reborn.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.reyzerbit.mca_reborn.api.API;
import com.reyzerbit.mca_reborn.common.MCA;
import com.reyzerbit.mca_reborn.common.items.Baby;
import com.reyzerbit.mca_reborn.network.MCAMessages.BabyName;
import com.reyzerbit.mca_reborn.network.Network;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NameBabyScreen extends Screen {

    private TextFieldWidget babyNameTextField;
    private Button doneButton;
    private Button randomButton;
    private Baby baby;

    public NameBabyScreen(ItemStack babyStack) {
    	
        super(new StringTextComponent("Name Baby"));

        if (babyStack.getItem() instanceof Baby) this.baby = (Baby) babyStack.getItem();
        
    }

    @Override
    public void tick() {
    	
        super.tick();
        
        babyNameTextField.tick();
        
        doneButton.active = !babyNameTextField.getValue().isEmpty();
        randomButton.active = true;
        
    }

    @Override
    public void init() {

        if (this.baby == null) this.onClose();
    	
    	this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        
        // Text Field
        babyNameTextField = new TextFieldWidget(font, width / 2 - 100, height / 2 - 60, 200, 20, new StringTextComponent("Enter name"));
        babyNameTextField.setMaxLength(32);
        babyNameTextField.setEditable(true);
        babyNameTextField.setFocus(true);
        children.add(babyNameTextField);
    	
    	// Done button
    	doneButton = addButton(new Button(width / 2 - 40, height / 2 - 10, 80, 20, new StringTextComponent(MCA.getLocalizer().localize("gui.button.done")), button -> {

            Network.INSTANCE.sendToServer(new BabyName(babyNameTextField.getValue().trim()));
            //doneButton.playDownSound(new SoundHandler());
            onClose();
        	
        }));
    	
    	// Random button
    	randomButton = addButton(new Button(width / 2 + 105, height / 2 - 60, 60, 20, new StringTextComponent(MCA.getLocalizer().localize("gui.button.random")), button -> {
    		
    		babyNameTextField.setValue(API.getRandomName(baby.getGender()));
    		//randomButton.playDownSound(new SoundHandler());
    	
    	}));
        
    }

    @Override
    public void onClose() {
    	
    	this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
        super.onClose();
        
    }

    @Override
    public boolean isPauseScreen() {
    	
        return false;
        
    }

    // TODO On another day
    
    /*
     * 
    @Override
	public boolean keyPressed(int key, int x, int y) {
    	
    	babyNameTextField.keyPressed(key, x, y);
        return super.keyPressed(key, x, y);
        
    }
    
    @Override
	public boolean handleComponentClicked(Style style) {
    	
    	if(style == null) return false;
    	
    	ClickEvent event = style.getClickEvent();
    	
    	if(event != null) {
    		
    	} else {
    		
    		return false;
    		
    	}

    	
        //babyNameTextField.mouseClicked(style., clickY, clicked);
        super.handleComponentClicked(style);
        
        return true;
        
    }
    */
    

    @Override
    public void render(MatrixStack matrix, int sizeX, int sizeY, float offset) {
    	
        renderBackground(matrix);
        drawString(matrix, font, MCA.getLocalizer().localize("gui.title.namebaby"), width / 2 - 100, height / 2 - 70, 0xa0a0a0);
        babyNameTextField.render(matrix, sizeX /*width / 2 - 100*/, sizeY /*height / 2 - 70*/, offset /*10F*/);
        super.render(matrix, sizeX, sizeY, offset);
        
    }
    
}