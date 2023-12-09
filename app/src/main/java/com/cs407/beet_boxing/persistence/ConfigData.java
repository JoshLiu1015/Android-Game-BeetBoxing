package com.cs407.beet_boxing.persistence;

import com.cs407.beet_boxing.util.EnumControlScheme;

/**
 * Object used to store the interesting config options from our settings menu.
 */
public class ConfigData {
    private int volume;
    private EnumControlScheme controlScheme;

    public ConfigData(int volumeMultiplier, EnumControlScheme controlScheme) {
        this.volume = volumeMultiplier;
        this.controlScheme = controlScheme;
    }

    public ConfigData() {
        this.volume = 1;
        this.controlScheme = EnumControlScheme.TILT;
    }

    public void setControlScheme(EnumControlScheme controlScheme) {
        this.controlScheme = controlScheme;
        PersistentInfo.saveConfig();
    }

    public void setVolume(int volume) {
        this.volume = volume;
        PersistentInfo.saveConfig();
    }

    public EnumControlScheme getControlScheme() {
        return controlScheme;
    }

    public int getVolume() {
        return volume;
    }
}
