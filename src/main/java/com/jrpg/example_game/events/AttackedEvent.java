package com.jrpg.example_game.events;

import com.jrpg.example_game.ExampleGameObject;
import com.jrpg.example_game.GameEvent;

public class AttackedEvent extends GameEvent {
    private ExampleGameObject attacker;

    public ExampleGameObject getAttacker() {
        return attacker;
    }

    public AttackedEvent(ExampleGameObject attacker) {
        this.attacker = attacker;
    }
}
