package com.reyzerbit.mca_reborn.client.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Timer;
import java.util.TimerTask;

import com.reyzerbit.mca_reborn.api.API;
import com.reyzerbit.mca_reborn.api.types.APIButton;
import com.reyzerbit.mca_reborn.client.gui.component.GuiButtonEx;
import com.reyzerbit.mca_reborn.common.entities.MCAVillager;
import com.reyzerbit.mca_reborn.network.MCAMessages;
import com.reyzerbit.mca_reborn.network.Network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VillagerEditorScreen extends Screen {
	
    private final MCAVillager villager;
    @SuppressWarnings("unused")
	private final PlayerEntity player;

    private TextFieldWidget nameTextField;
    private TextFieldWidget professionTextField;
    private TextFieldWidget textureTextField;

    public VillagerEditorScreen(MCAVillager EntityHuman, PlayerEntity player) {
    	
        super(new StringTextComponent("Villager Editor"));
        this.player = player;
        villager = EntityHuman;
        
    }

    @Override
    public void tick() {
    	
        super.tick();
        nameTextField.moveCursorToEnd();
        professionTextField.moveCursorToEnd();
        textureTextField.moveCursorToEnd();
        
    }

    @Override
    public void init() {
    	
    	//TODO Figure this out
        //Keyboard.enableRepeatEvents(true);
    	
        //drawEditorGui();

        nameTextField = new TextFieldWidget(font, width / 2 - 205, height / 2 - 95, 150, 20, new StringTextComponent("Name"));
        nameTextField.setMaxLength(32);
        nameTextField.setMessage(new StringTextComponent(villager.get(MCAVillager.VILLAGER_NAME)));
        professionTextField = new TextFieldWidget(font, width / 2 - 190, height / 2 + 10, 250, 20, new StringTextComponent("Job"));
        professionTextField.setMaxLength(64);
        professionTextField.setMessage(new StringTextComponent(villager.getVillagerData().getProfession().getRegistryName().toString()));
        textureTextField = new TextFieldWidget(font, width / 2 - 190, height / 2 - 15, 250, 20, new StringTextComponent("Texture"));
        textureTextField.setMaxLength(128);
        textureTextField.setMessage(new StringTextComponent(villager.get(MCAVillager.TEXTURE)));
        
    }

    @Override
    public void onClose() {
    	
    	// TODO Figure this out
        //Keyboard.enableRepeatEvents(false);
        
    }

    protected void actionPerformed(Button guiButton) {
    	
        APIButton btn = ((GuiButtonEx) guiButton).getApiButton();
        
        if (btn.isNotifyServer()) {
        	
            Network.INSTANCE.sendToServer(new MCAMessages.ButtonAction("editor", btn.getIdentifier(), villager.getUUID()));
            
        } else if (btn.getIdentifier().equals("gui.button.done")) {
        	
            minecraft.setScreen(null);
            
        } else if (btn.getIdentifier().equals("gui.button.copyuuid")) {
        	
        	Minecraft mc = Minecraft.getInstance();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(villager.getUUID().toString()), null);
            mc.player.sendMessage(new StringTextComponent("Villager UUID copied to clipboard."), mc.player.getUUID());
            
        } else if (btn.getIdentifier().equals("gui.button.profession.set")) {
        	
            String profession = professionTextField.getValue();
            Network.INSTANCE.sendToServer(new MCAMessages.SetProfession(profession, villager.getUUID()));
            
            new Timer().schedule(new TimerTask() {
            	
                        @Override
                        public void run() {
                        	
                        	Network.INSTANCE.sendToServer(new MCAMessages.CareerRequest(villager.getUUID()));
                            
                        }
                    },500
            );
            
        } else if (btn.getIdentifier().contains("gui.button.texture")) {
        	
            String texture = btn.getIdentifier().endsWith(".set") ? textureTextField.getValue() : API.getRandomSkin(villager);
            Network.INSTANCE.sendToServer(new MCAMessages.SetTexture(texture, villager.getUUID()));
            textureTextField.setValue(texture);
            
        }
        
    }

    @Override
	public boolean keyPressed(int key, int x, int y) {
    	
        if (key == 256) {
        	
            Minecraft.getInstance().setScreen(null);
            
        } else {
        	
            if (nameTextField.keyPressed(key, x, y)) {
            	
                String text = nameTextField.getValue().trim();
                Network.INSTANCE.sendToServer(new MCAMessages.SetName(text, villager.getUUID()));
                
            }
            
            textureTextField.keyPressed(key, x, y);
            professionTextField.keyPressed(key, x, y);
            //drawEditorGui();
            
        }
        
        return true;
        
    }

    // TODO The rest
    /*
    @Override
    protected void mouseClicked(int clickX, int clickY, int clicked) throws IOException {
    	
        super.mouseClicked(clickX, clickY, clicked);
        nameTextField.mouseClicked(clickX, clickY, clicked);
        professionTextField.mouseClicked(clickX, clickY, clicked);
        textureTextField.mouseClicked(clickX, clickY, clicked);
        
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void drawEditorGui() {
        buttonList.clear();
        API.addButtons("editor", villager, player, this);
    }

    @Override
    public void drawScreen(int sizeX, int sizeY, float offset) {
        drawGradientRect(0, 0, width, height, -1072689136, -804253680);
        drawString(fontRenderer, "Name:", width / 2 - 205, height / 2 - 110, 0xffffff);
        drawCenteredString(fontRenderer, MCA.getLocalizer().localize("gui.title.editor"), width / 2, height / 2 - 110, 0xffffff);
        nameTextField.drawTextBox();
        professionTextField.drawTextBox();
        textureTextField.drawTextBox();
        super.drawScreen(sizeX, sizeY, offset);
    }
    */
}