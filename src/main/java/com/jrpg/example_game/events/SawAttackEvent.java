package com.jrpg.example_game.events;

import com.jrpg.example_game.ExampleGameObject;
import com.jrpg.example_game.GameEvent;
public class SawAttackEvent extends GameEvent {
    private ExampleGameObject attacker;
    private ExampleGameObject defender;

    public ExampleGameObject getAttacker() {
        return attacker;
    }

    public ExampleGameObject getDefender() {
        return defender;
    }

    public SawAttackEvent(ExampleGameObject attacker, ExampleGameObject defender){
        this.attacker = attacker;
        this.defender = defender;
    }
}
