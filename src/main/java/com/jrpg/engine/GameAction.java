package com.jrpg.engine;

import java.util.function.Consumer;

public class GameAction {
    private String name;
    private DialogueLine description;
    private Consumer<Engine> onRun;

    public String getName() {
        return name;
    }

    public DialogueLine getDescription() {
        return description;
    }

    public Consumer<Engine> getOnRun() {
        return onRun;
    }

    public GameAction(String name, DialogueLine description, Consumer<Engine> onRun){
        this.name = name;
        this.description = description;
        this.onRun = onRun;
    }
}
