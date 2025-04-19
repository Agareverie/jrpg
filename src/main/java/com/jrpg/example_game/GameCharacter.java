package com.jrpg.example_game;

import java.util.ArrayList;
import java.util.List;

public class GameCharacter extends ExampleGameObject {
    private final List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return items;
    }

    public GameCharacter(String name, int Health, GameStats baseStats){
        super(name, Health, baseStats);
    }

    @Override
    public GameStats getEffectiveStats() {
        GameStats baseStats = getBaseStats();
        int attack = baseStats.attack();
        int defense = baseStats.defense();
        int accuracy = baseStats.accuracy();
        int evasion = baseStats.evasion();

        for(Item item : items){
            GameStats itemStats = item.getStats();
            attack += itemStats.attack();    
            defense += itemStats.defense();    
            accuracy += itemStats.accuracy();    
            evasion += itemStats.evasion();    
        }

        return new GameStats(attack, defense, accuracy, evasion);
    }
    
    public void addItem(Item item){
        this.items.add(item);
    }
}
