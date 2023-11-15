package com.cs407.beet_boxing.util;

import java.util.Map;

/**
 * This just holds a field to make some GSON deserialization easier, because I can't figure out
 * Why things weren't working. Might change this later
 */
public class DummyInventory {
    public Map<EnumProduceType, Integer> collected;

    public DummyInventory(Map<EnumProduceType, Integer> collected) {
        this.collected = collected;
    }
}
