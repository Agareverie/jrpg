package com.jrpg.engine.components;

import com.jrpg.engine.Engine;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GameAction {
    private String name;
    private Dialogue description;
    private final BiConsumer<Engine, GameAction> onRun;
    private final Predicate<GameObject> condition;
    private boolean closesMenu = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dialogue getDescription() {
        return description;
    }

    public void setDescription(Dialogue description) {
        this.description = description;
    }

    public boolean closesMenu(){
        return closesMenu;
    }

    public void setClosesMenu(boolean closesMenu) {
        this.closesMenu = closesMenu;
    }

    public Consumer<Engine> getOnRun() {
        return (engine) -> {onRun.accept(engine, this);};
    }

    public Predicate<GameObject> getCondition() {
        return condition;
    }

    public GameAction(String name, Dialogue description, Consumer<Engine> onRun) {
        this(name, description, (engine, action) -> {onRun.accept(engine);}, (gameObject) -> true);
    }

    public GameAction(String name, Dialogue description, Consumer<Engine> onRun, Predicate<GameObject> condition) {
        this(name, description, (engine, action) -> {onRun.accept(engine);}, condition);
    }

    public GameAction(String name, Dialogue description, BiConsumer<Engine, GameAction> onRun) {
        this(name, description, onRun, (gameObject) -> true);
    }

    public GameAction(String name, Dialogue description, BiConsumer<Engine, GameAction> onRun, Predicate<GameObject> condition) {
        this.name = name;
        this.description = description;
        this.onRun = onRun;
        this.condition = condition;
    }
}
