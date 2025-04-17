package com.jrpg.engine;

import java.util.function.Consumer;

public class GameAction {
    private String name;
    private Dialogue description;
    private Consumer<Engine> onRun;

    public String getName() {
        return name;
    }

    public Dialogue getDescription() {
        return description;
    }

    public Consumer<Engine> getOnRun() {
        return onRun;
    }

    public GameAction(String name, Consumer<Engine> onRun){
        this.name = name;
        this.onRun = onRun;
    }
}
