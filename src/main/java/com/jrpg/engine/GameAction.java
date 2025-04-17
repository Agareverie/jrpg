package com.jrpg.engine;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class GameAction {
    private String name;
    private Dialogue description;
    private Consumer<Engine> onRun;
    private Predicate<GameObject> condition;

    public String getName() {
        return name;
    }

    public Dialogue getDescription() {
        return description;
    }

    public Consumer<Engine> getOnRun() {
        return onRun;
    }

    public Predicate<GameObject> getCondition() {
        return condition;
    }

    public GameAction(String name, Consumer<Engine> onRun){
        this(name, onRun, (gameObject) -> true);
    }

    public GameAction(String name, Consumer<Engine> onRun, Predicate<GameObject> condition){
        this.name = name;
        this.onRun = onRun;
        this.condition = condition;
    }
}
