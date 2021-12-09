package com.reyzerbit.mca_reborn.client.gui;

import java.util.Optional;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.reyzerbit.mca_reborn.api.API;
import com.reyzerbit.mca_reborn.client.gui.component.GuiButtonEx;
import com.reyzerbit.mca_reborn.common.Constants;
import com.reyzerbit.mca_reborn.common.MCA;
import com.reyzerbit.mca_reborn.common.data.ParentData;
import com.reyzerbit.mca_reborn.common.data.PlayerHistory;
import com.reyzerbit.mca_reborn.common.entities.MCAVillager;
import com.reyzerbit.mca_reborn.common.enums.EnumMarriageState;
import com.reyzerbit.mca_reborn.common.enums.EnumMoveState;
import com.reyzerbit.mca_reborn.network.MCAMessages.ButtonAction;
import com.reyzerbit.mca_reborn.network.Network;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VillagerInteractionScreen extends Screen {
	
    private static final ResourceLocation ICON_TEXTURES = new ResourceLocation("mca_reborn:textures/gui.png");
    private static boolean displaySuccessChance;
    private final MCAVillager villager;
    private final PlayerEntity player;

    private boolean inGiftMode;

    private int timeSinceLastClick;

    private int marriedIconU = 0;
    private int engagedIconU = 64;
    private int notMarriedIconU = 16;
    private int parentsIconU = 32;
    private int giftIconU = 48;
    private int redHeartIconU = 80;
    private int blackHeartIconU = 96;
    private int goldHeartIconU = 112;

    private int mouseX;
    private int mouseY;

    // Tracks which page we're on in the GUI for sending button events
    private String activeKey;

	public VillagerInteractionScreen(ITextComponent title, MCAVillager villager, PlayerEntity player) {
		
		super(title);
        this.villager = villager;
        this.player = player;
        this.activeKey = "main";
		
	}

	@Override
    public void init() {
    	
        drawMainButtonMenu();
        
    }

    private void drawMainButtonMenu() {
    	
        buttons.clear();
        API.addButtons("main", villager, player, this);

        EnumMoveState moveState = EnumMoveState.byId(villager.get(MCAVillager.MOVE_STATE));
        if (moveState == EnumMoveState.FOLLOW) disableButton("gui.button.follow");
        else if (moveState == EnumMoveState.STAY) disableButton("gui.button.stay");
        else if (moveState == EnumMoveState.MOVE) disableButton("gui.button.move");
        
    }

    private void disableButton(String id) {
    	
        Optional<GuiButtonEx> b = API.getButton(id, this);

        b.ifPresent(guiButtonEx -> guiButtonEx.active = false);
        
    }

    @Override
    public boolean isPauseScreen() {
    	
        return false;
        
    }
    
    private void drawIcons(MatrixStack matrix) {
    	
        PlayerHistory history = villager.getPlayerHistoryFor(player.getUUID());
        EnumMarriageState marriageState = EnumMarriageState.byId(villager.get(MCAVillager.MARRIAGE_STATE));
        
        int marriageIconU = marriageState == EnumMarriageState.MARRIED ? marriedIconU : marriageState == EnumMarriageState.ENGAGED ? engagedIconU : notMarriedIconU;
        int heartIconU = history.getHearts() < 0 ? blackHeartIconU : history.getHearts() >= 100 ? goldHeartIconU : redHeartIconU;

        matrix.pushPose();
        {
        	
            // ??? GL11.glColor3f(255.0F, 255.0F, 255.0F);
            matrix.scale(2.0F, 2.0F, 2.0F);

            this.minecraft.getTextureManager().bind(ICON_TEXTURES);
            this.blit(matrix, 5, 15, heartIconU, 0, 16, 16);
            this.blit(matrix, 5, 30, marriageIconU, 0, 16, 16);

            if (canDrawParentsIcon()) this.blit(matrix, 5, 45, parentsIconU, 0, 16, 16);

            if (canDrawGiftIcon()) this.blit(matrix, 5, 60, giftIconU, 0, 16, 16);
            
        }
        
        matrix.popPose();
        
    }

    private boolean canDrawParentsIcon() {
    	
        ParentData data = ParentData.fromNBT(villager.get(MCAVillager.PARENTS));
        
        return !data.getParent1UUID().equals(Constants.ZERO_UUID) && !data.getParent2UUID().equals(Constants.ZERO_UUID);
        
    }

    private boolean canDrawGiftIcon() {
    	
        return villager.getPlayerHistoryFor(player.getUUID()).isGiftPresent();
        
    }

    @Override
    public void render(MatrixStack matrix, int i, int j, float f) {
    	
        super.render(matrix, i, j, f);
        
        drawIcons(matrix);
        drawTextPopups(matrix);

        // TODO Mouse stuff
        /*
        mouseX = Mouse.getEventX() * width / mc.displayWidth;
        mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
        */
        
    }

    private void drawTextPopups(MatrixStack matrix) {
    	
        EnumMarriageState marriageState = EnumMarriageState.byId(villager.get(MCAVillager.MARRIAGE_STATE));
        String marriageInfo;

        if (hoveringOverHeartsIcon()) {
        	
            int hearts = villager.getPlayerHistoryFor(player.getUUID()).getHearts();
            this.renderTooltip(matrix, new StringTextComponent(hearts + " hearts"), 35, 55);
            
        }

        if (hoveringOverMarriageIcon()) {
        	
            String spouseName = villager.get(MCAVillager.SPOUSE_NAME);
            if (marriageState == EnumMarriageState.MARRIED) marriageInfo = MCA.getLocalizer().localize("gui.interact.label.married", spouseName);
            else if (marriageState == EnumMarriageState.ENGAGED) marriageInfo = MCA.getLocalizer().localize("gui.interact.label.engaged", spouseName);
            else marriageInfo = MCA.getLocalizer().localize("gui.interact.label.notmarried");

            this.renderTooltip(matrix, new StringTextComponent(marriageInfo), 35, 85);
            
        }
        
        if (canDrawParentsIcon() && hoveringOverParentsIcon()) {
        	
            ParentData data = ParentData.fromNBT(villager.get(MCAVillager.PARENTS));
            this.renderTooltip(matrix, new StringTextComponent(MCA.getLocalizer().localize("gui.interact.label.parents")).append(data.getParent1Name()).append(data.getParent2Name()), 35, 115);
            
        }

        if (canDrawGiftIcon() && hoveringOverGiftIcon()) this.renderTooltip(matrix, new StringTextComponent(MCA.getLocalizer().localize("gui.interact.label.gift")), 35, 145);
        
    }

    protected void actionPerformed(Button button) {
    	
        GuiButtonEx btn = (GuiButtonEx) button;
        String id = btn.getApiButton().getIdentifier();

        if (timeSinceLastClick <= 2) {
            
        	return; // Prevents click-throughs on Mojang's button system
        	
        }
        
        timeSinceLastClick = 0;

        // Progression to different GUIs 
        if (id.equals("gui.button.interact")) {
        	
            activeKey = "interact";
            drawInteractButtonMenu();
            return;
            
        } else if (id.equals("gui.button.work")) {
        	
            activeKey = "work";
            drawWorkButtonMenu();
            return;
            
        } else if (id.equals("gui.button.backarrow")) {
        	
            drawMainButtonMenu();
            activeKey = "main";
            return;
            
        } else if (id.equals("gui.button.location")) {
        	
            activeKey = "location";
            drawLocationButtonMenu();
            return;
            
        }

        // Anything that should notify the server is handled here 
        else if (btn.getApiButton().isNotifyServer()) {
        	
            Network.INSTANCE.sendToServer(new ButtonAction(activeKey, id, villager.getUUID()));
            
        } else if (id.equals("gui.button.gift")) {
        	
            this.inGiftMode = true;
            disableAllButtons();
            return;
            
        }

        this.minecraft.setScreen(null);
        
    }

    @Override
    public void tick() {
    	
        if (timeSinceLastClick < 100) {
        	
            timeSinceLastClick++;
            
        }
        
    }

    private void disableAllButtons() {
        buttons.forEach((b) -> b.active = false);
    }

    private void drawInteractButtonMenu() {
        buttons.clear();
        API.addButtons("interact", villager, player, this);
    }

    private void drawWorkButtonMenu() {
    	buttons.clear();
        API.addButtons("work", villager, player, this);
    }

    private void drawLocationButtonMenu() {
    	buttons.clear();
        API.addButtons("location", villager, player, this);
    }

    private void enableAllButtons() {
    	buttons.forEach((b) -> b.active = true);
    }

    private boolean hoveringOverHeartsIcon() {
        return mouseX <= 32 && mouseX >= 16 && mouseY >= 32 && mouseY <= 48;
    }

    private boolean hoveringOverMarriageIcon() {
        return mouseX <= 32 && mouseX >= 16 && mouseY >= 66 && mouseY <= 81;
    }

    private boolean hoveringOverParentsIcon() {
        return mouseX <= 32 && mouseX >= 16 && mouseY >= 100 && mouseY <= 115;
    }

    private boolean hoveringOverGiftIcon() {
        return mouseX <= 32 && mouseX >= 16 && mouseY >= 124 && mouseY <= 148;
    }
	
	/*

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        if (Mouse.getEventDWheel() < 0) {
            player.inventory.currentItem = player.inventory.currentItem == 8 ? 0 : player.inventory.currentItem + 1;
        } else if (Mouse.getEventDWheel() > 0) {
            player.inventory.currentItem = player.inventory.currentItem == 0 ? 8 : player.inventory.currentItem - 1;
        }
    }

    @Override
    protected void mouseClicked(int posX, int posY, int button) throws IOException {
        super.mouseClicked(posX, posY, button);

        // Right mouse button
        if (inGiftMode && button == 1) NetMCA.INSTANCE.sendToServer(new NetMCA.ButtonAction(activeKey, "gui.button.gift", villager.getUniqueID()));
    }

    @Override
    protected void keyTyped(char keyChar, int keyCode) {
        // Hotkey to leave gift mode
        if (keyCode == Keyboard.KEY_ESCAPE) {
            if (inGiftMode) {
                inGiftMode = false;
                enableAllButtons();
            } else {
                this.mc.displayGuiScreen(null);
            }
        } else if (keyCode == Keyboard.KEY_LCONTROL) {
            displaySuccessChance = !displaySuccessChance;
        } else {
            try {
                int intInput = Integer.parseInt(String.valueOf(keyChar));

                if (intInput > 0) {
                    player.inventory.currentItem = intInput - 1;
                }
            } catch (NumberFormatException ignored) {
                // When a non numeric character is entered.
            }
        }
    }
    */
}