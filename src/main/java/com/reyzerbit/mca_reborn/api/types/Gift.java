package com.reyzerbit.mca_reborn.api.types;

import com.reyzerbit.mca_reborn.common.MCA;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.item.Item;

@AllArgsConstructor
@Getter
public class Gift {
	
    private String name;
    private int id;

    /**
     * Used for verifying if a given gift exists in the game's registries.
     * @return True if the item/block exists.
     */
    public boolean exists() {
    	
        if (getName() != null || !getName().equals("")) {
        	
        	return Item.byId(getId()) != null;
            
        } else {
        	
            MCA.getLog().warn("Could not process gift '" + getName() + "'- bad type id of '" + getId() + "'. Must be 'item' or 'block'");
            return false;
            
        }
        
    }
    
}
