package com.jrpg.engine;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConditionalGameAction extends GameAction {
    Predicate<GameObject> condition;
    public Predicate<GameObject> getCondition() {
        return condition;
    }

    static ConditionalGameAction toConditional(GameAction gameAction, Predicate<GameObject> condition){
        return new ConditionalGameAction(gameAction.getName(), gameAction.getOnRun(), condition);
    }
    ConditionalGameAction(String name, Consumer<Engine> onRun, Predicate<GameObject> condition){
        super(name, onRun);
        this.condition = condition;
    }
}
