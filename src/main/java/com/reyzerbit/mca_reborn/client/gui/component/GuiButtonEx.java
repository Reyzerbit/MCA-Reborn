package com.reyzerbit.mca_reborn.client.gui.component;

import com.reyzerbit.mca_reborn.api.types.APIButton;
import com.reyzerbit.mca_reborn.common.MCA;

import lombok.Getter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class GuiButtonEx extends Button {
	
    @Getter private APIButton apiButton;

    public GuiButtonEx(Screen gui, APIButton apiButton) {
    	
        super((gui.width / 2) + apiButton.getX(), (gui.height / 2) + apiButton.getY(), apiButton.getWidth(), apiButton.getHeight(), new StringTextComponent(MCA.getLocalizer().localize(apiButton.getIdentifier())), null);
        this.apiButton = apiButton;
        
    }
}
