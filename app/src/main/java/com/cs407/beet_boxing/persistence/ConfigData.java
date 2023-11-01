package com.cs407.beet_boxing.persistence;

import com.cs407.beet_boxing.util.EnumControlScheme;

/**
 * Object used to store the interesting config options from our settings menu.
 */
public class ConfigData {
    public float volumeMultiplier;
    public EnumControlScheme controlScheme;

    public ConfigData(float volumeMultiplier, EnumControlScheme controlScheme) {
        this.volumeMultiplier = volumeMultiplier;
        this.controlScheme = controlScheme;
    }

    public ConfigData() {
        this.volumeMultiplier = 1f;
        this.controlScheme = EnumControlScheme.TILT;
    }
}
