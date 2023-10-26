package com.cs407.beet_boxing.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.cs407.beet_boxing.util.EnumControlScheme;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

/**
 * This is the main class that is intended to be interacted with when dealing with any data
 * that needs to be stored between sessions. Currently, the values of the config and gameData
 * are saved as instance variables and can be modified as you wish. The correct implementation of
 * this method will handle the storing/loading without the user having to worry about it.
 * <p></p>
 * Note: currently this remains untested as I have nothing to really test it with on the main branch.
 */
public class PersistentInfo {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static ConfigData config;
    public static GameData gameData;

    /**
     * Tries to load data from config, else creates some defaults. Should be run on Application
     * start.
     *
     * @return status code
     */
    public static int init(Context context) {
        SharedPreferences preferences = preferences(context);
        try {
            config = GSON.fromJson(preferences.getString("config", ""), ConfigData.class);
        } catch (Exception e) {
            // Defaults
            config = new ConfigData(1f, EnumControlScheme.TILT);
            return 1;
        }

        try {
            gameData = GSON.fromJson(preferences.getString("gameData", ""), GameData.class);
        } catch (Exception e) {
            gameData = new GameData(Map.of(), 0);
            return 2;
        }

        return 0;
    }

    // startRegion save
    public static void saveGameData(Context context) {
        SharedPreferences preferences = preferences(context);
        preferences.edit().putString("gameData", GSON.toJson(gameData)).apply();
    }
    public static void saveConfig(Context context) {
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
}
