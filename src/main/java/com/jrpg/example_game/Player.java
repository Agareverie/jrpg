package com.jrpg.example_game;

import com.jrpg.engine.components.Dialogue;

public class Player extends GameCharacter {
    public Player(int health, GameStats baseStats){
        super("player", health, baseStats);
        setDescription(Dialogue.fromString("You"));

    }
}
