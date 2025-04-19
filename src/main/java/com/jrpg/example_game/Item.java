package com.jrpg.example_game;

public class Item {
    private GameStats stats;
    private String name;

    public String getName() {
        return name;
    }

    public GameStats getStats() {
        return stats;
    }

    public Item(String name, GameStats stats) {
        this.name = name;
        this.stats = stats;
    }
}
