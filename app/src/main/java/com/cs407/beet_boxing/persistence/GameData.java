package com.cs407.beet_boxing.persistence;

import com.cs407.beet_boxing.util.EnumProduceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Object used to store relevant game stats
 */
public class GameData {
    public Map<EnumProduceType, Integer> inventory;
    public Integer highScore;

    public GameData(Map<EnumProduceType, Integer> inventory, Integer highScore) {
        this.inventory = inventory;
        this.highScore = highScore;
    }

    public GameData() {
        this.inventory = new HashMap<>();
        this.highScore = 0;
    }
}
