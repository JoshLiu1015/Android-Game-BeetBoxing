package com.cs407.beet_boxing.util;

import com.cs407.beet_boxing.R;

public enum EnumProduceType {
    CARROT(R.id.fallingCarrot),
    BANANA(R.id.fallingBanana),
    APPLE(R.id.fallingApple),
    POTATO(R.id.fallingPotato),
    ONION(R.id.fallingOnion),
    ORANGE(R.id.fallingOrange),
    MELON(R.id.fallingMelon),
    GINGER(R.id.fallingGinger),
    BEET(R.id.fallingBeet);

    private final Integer id;

    EnumProduceType(int id) {
        this.id = id;
    }

    /**
     * Can't use a switch here because of required constants, which is why this looks so
     * stinky.
     */
    public static EnumProduceType getById(int id) {
        if (id == R.id.fallingCarrot) {
            return CARROT;
        } else if (id == R.id.fallingBanana) {
            return BANANA;
        } else if (id == R.id.fallingApple) {
            return APPLE;
        } else if (id == R.id.fallingPotato) {
            return POTATO;
        } else if (id == R.id.fallingOnion) {
            return ONION;
        } else if (id == R.id.fallingOrange) {
            return ORANGE;
        } else if (id == R.id.fallingMelon) {
            return MELON;
        } else if (id == R.id.fallingGinger) {
            return GINGER;
        } else if (id == R.id.fallingBeet) {
            return BEET;
        }

        return null;
    }
}
