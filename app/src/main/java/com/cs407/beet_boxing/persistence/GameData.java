package com.cs407.beet_boxing.persistence;

import com.cs407.beet_boxing.util.EnumProduceType;

import java.io.Serializable;
import java.util.Map;

public record GameData(
    Map<EnumProduceType, Integer> inventory,
    Integer highScore
) implements Serializable {
}
