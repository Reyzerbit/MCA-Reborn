package com.reyzerbit.mca_reborn.common.data;

import java.util.UUID;

import com.reyzerbit.mca_reborn.common.enums.EnumMarriageState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayerData {
	
	private UUID player;

    private UUID spouseUUID;
    private EnumMarriageState marriageState;
    private String spouseName;
    private boolean babyPresent;

}
