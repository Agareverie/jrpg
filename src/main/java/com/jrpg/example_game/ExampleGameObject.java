package com.jrpg.example_game;

import java.util.HashSet;
import java.util.Set;

import com.jrpg.engine.components.*;

//example of how you can extend the gameObject to fit any functionality the
//game needs

public class ExampleGameObject extends GameObject {
    private int health;
    private final String name;
    private final GameStats baseStats;
    private final GameEventListenerManager gameEventListenerManager = new GameEventListenerManager();
    private final Set<String> tags = new HashSet<>();

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

    public ExampleGameObject(String name, int health, GameStats baseStats) {
        super(Vector2D.zero(), Vector2D.zero(), null);
        this.name = name;
        this.health = health;
        this.baseStats = baseStats;

        getGameEventListenerManager().registerEventListener(new GameEventListener("Death", (gameEvent, engine)->{
            engine.enqueueDialogue(Dialogue.fromString(name + " died"));
            setSelectable(false);
            setSpriteName(null);
        }));
    }

    public GameStats getEffectiveStats() {
        return baseStats;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public boolean hasTag(String tag) {
        return this.tags.contains(tag);
    }
}
