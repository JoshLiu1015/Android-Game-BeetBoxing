package com.cs407.beet_boxing.persistence;

public class PersistentInfo {

    public static ConfigData config;
    public static GameData gameData;

    // startRegion save
    public static void saveGameData() {
        // TODO
    }

    public static void saveConfig() {
        // TODO
    }
    // endRegion save

    /**
     * Tries to load data from config, else creates some defaults. Should be run on Application
     * start.
     * @return status code
     */
    public static int init() {
        // TODO
        return -1;
    }
}
