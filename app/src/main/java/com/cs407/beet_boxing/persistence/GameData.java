package com.cs407.beet_boxing.persistence;

import androidx.annotation.NonNull;

import com.cs407.beet_boxing.util.EnumProduceType;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Object used to store relevant game stats
 */
public class GameData {
    @NonNull
    private Map<EnumProduceType, Integer> inventory;
    @NotNull
    private Integer highScore;

    public GameData(Map<EnumProduceType, Integer> inventory, Integer highScore) {
        this.inventory = inventory;
        this.highScore = highScore;
    }

    public GameData() {
        this.inventory = new HashMap<>();
        this.highScore = 0;
    }

    public void setHighScore(Integer highScore) {
        this.highScore = highScore;
        PersistentInfo.saveGameData();
    }

    public void setInventory(Map<EnumProduceType, Integer> inventory) {
        this.inventory = inventory;
        PersistentInfo.saveGameData();
    }

    @NotNull
    public Integer getHighScore() {
        return highScore;
    }

    @NotNull
    public Map<EnumProduceType, Integer> getInventory() {
        return inventory;
    }
}
