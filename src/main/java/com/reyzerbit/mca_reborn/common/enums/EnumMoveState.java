package com.reyzerbit.mca_reborn.common.enums;

import java.util.Arrays;

import com.reyzerbit.mca_reborn.common.MCA;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EnumMoveState {
    MOVE(0, ""),
    STAY(1, "gui.label.staying"),
    FOLLOW(2, "gui.label.following");

    @Getter int id;
    String friendlyName;

    public static EnumMoveState byId(int id) {
        return Arrays.stream(values()).filter(s -> s.id == id).findFirst().orElse(MOVE);
    }

    public String getFriendlyName() {
        return MCA.getLocalizer().localize(friendlyName);
    }
}

