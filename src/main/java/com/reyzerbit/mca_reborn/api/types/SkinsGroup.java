package com.reyzerbit.mca_reborn.api.types;

import com.reyzerbit.mca_reborn.common.enums.EnumGender;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SkinsGroup {
    private String gender;
    @Getter private String profession;
    @Getter private String[] paths;

    public EnumGender getGender() {
    	
        return EnumGender.byName(gender);
        
    }
}
