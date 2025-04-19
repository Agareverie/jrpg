package com.jrpg.example_game;

import java.util.ArrayList;
import java.util.List;

import com.jrpg.engine.Engine;
import com.jrpg.engine.components.*;
import com.jrpg.example_game.events.DeathEvent;

//example of how you can extend the gameObject to fit any functionality the
//game needs

public class ExampleGameObject extends GameObject {
    private int health;
    private String name;
    private GameStats baseStats;
    private GameEventListenerManager gameEventListenerManager = new GameEventListenerManager();
    private List<String> tags = new ArrayList<String>();

    public GameStats getBaseStats() {
        return baseStats;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public GameEventListenerManager getGameEventListenerManager() {
        return gameEventListenerManager;
    }

    public ExampleGameObject(String name, int health, GameStats baseStats){
        super(Vector2D.zero(), Vector2D.zero(), null);
        this.name = name;
        this.health = health;
        this.baseStats = baseStats;

        getGameEventListenerManager().registerEventListener(new GameEventListener<DeathEvent>(){
            @Override
            protected void run (DeathEvent deathEvent, Engine engine){
                engine.enqueueDialogue(Dialogue.fromString(name + " died"));
                setSelectable(false);
                setSpriteName(null);
            }
        });
    }

    public GameStats getEffectiveStats(){
        return baseStats;
    }

    public void addTag(String tag){
        this.tags.add(tag);
    }

    public boolean hasTag(String tag){
        return this.tags.contains(tag);
    }
}
