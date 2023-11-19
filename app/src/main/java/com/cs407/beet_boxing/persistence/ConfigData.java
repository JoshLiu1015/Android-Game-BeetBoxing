package com.cs407.beet_boxing.persistence;

import com.cs407.beet_boxing.util.EnumControlScheme;

/**
 * Object used to store the interesting config options from our settings menu.
 */
public class ConfigData {
    public int volume;
    public EnumControlScheme controlScheme;

    public ConfigData(int volumeMultiplier, EnumControlScheme controlScheme) {
        this.volume = volumeMultiplier;
        this.controlScheme = controlScheme;
    }

    public ConfigData() {
        this.volume = 1;
        this.controlScheme = EnumControlScheme.TILT;
    }
}
