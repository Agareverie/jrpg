package com.jrpg.engine.components;

import com.jrpg.engine.Engine;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class GameAction {
    private final String name;
    private Dialogue description;
    private final Consumer<Engine> onRun;
    private final Predicate<GameObject> condition;

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

    public GameAction(String name, Consumer<Engine> onRun) {
        this(name, onRun, (gameObject) -> true);
    }

    public GameAction(String name, Consumer<Engine> onRun, Predicate<GameObject> condition) {
        this.name = name;
        this.onRun = onRun;
        this.condition = condition;
    }
}
