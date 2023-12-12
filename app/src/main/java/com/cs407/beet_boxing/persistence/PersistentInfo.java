package com.cs407.beet_boxing.persistence;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This is the main class that is intended to be interacted with when dealing with any data
 * that needs to be stored between sessions. Currently, the values of the config and gameData
 * are saved as instance variables and can be modified as you wish. The correct implementation of
 * this method will handle the storing/loading without the user having to worry about it.
 */
public class PersistentInfo {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static ConfigData config;
    private static GameData gameData;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    /**
     * Tries to load data from config, else creates some defaults. Should be run on Application
     * start.
     *
     * @return status code
     */
    public static int init(Context context) {
        PersistentInfo.context = context;
        if (config != null) {
            return 3;
        }

        int result = 0;
        SharedPreferences preferences = preferences(PersistentInfo.context);
        try {
            config = GSON.fromJson(preferences.getString("config", ""), ConfigData.class);
            if (config == null) {
                config = new ConfigData();
            }
        } catch (Exception e) {
            // Defaults
            config = new ConfigData();
            result = 1;
        }

        try {
            gameData = GSON.fromJson(preferences.getString("gameData", ""), GameData.class);
            if (gameData == null) {
                gameData = new GameData();
            }
        } catch (Exception e) {
            gameData = new GameData();
            result = 2;
        }

        saveConfig();
        saveGameData();
        return result;
    }

    // startRegion save
    public static void saveGameData() {
        SharedPreferences preferences = preferences(context);
        preferences.edit().putString("gameData", GSON.toJson(gameData)).apply();
    }

    public static void saveConfig() {
        SharedPreferences preferences = preferences(context);
        preferences.edit().putString("config", GSON.toJson(config)).apply();
    }
    // endRegion save

    /**
     * Helper method is used here instead of instance variable to avoid intellij complaining
     * about memory leaks regarding Context.
     */
    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    public static void setConfig(ConfigData config) {
        PersistentInfo.config = config;
        saveConfig();
    }

    public static void setGameData(GameData gameData) {
        PersistentInfo.gameData = gameData;
        saveGameData();
    }

    public static ConfigData getConfig() {
        return config;
    }

    public static GameData getGameData() {
        return gameData;
    }
}
