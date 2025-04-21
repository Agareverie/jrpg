package com.jrpg.example_game;

import com.jrpg.engine.components.Dialogue;

public class Player extends GameCharacter {
    private int money = 0;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Player(int health, GameStats baseStats){
        super("player", health, baseStats);
        setDescription(Dialogue.fromString("You"));

    }
}
