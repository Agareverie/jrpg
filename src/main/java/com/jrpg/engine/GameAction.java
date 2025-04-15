package com.jrpg.engine;

import java.util.function.Consumer;

public class GameAction {
    private String name;
    private String description;
    private Consumer<Engine> onRun;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Consumer<Engine> getOnRun() {
        return onRun;
    }

    public GameAction(String name, String description, Consumer<Engine> onRun){
        this.name = name;
        this.description = description;
        this.onRun = onRun;
    }
}
