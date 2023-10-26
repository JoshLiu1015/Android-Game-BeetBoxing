package com.cs407.beet_boxing.persistence;

import com.cs407.beet_boxing.util.EnumControlScheme;

import java.io.Serializable;

/**
 * Object used to store the interesting config options from our settings menu.
 */
public record ConfigData(
        float volumeMultiplier,
        EnumControlScheme controlScheme
) implements Serializable {

}
