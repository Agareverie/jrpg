package com.jrpg.example_game;

import com.jrpg.engine.Engine;
import com.jrpg.engine.components.*;
import com.jrpg.example_game.events.AttackedEvent;
import com.jrpg.example_game.events.SawAttackEvent;

public class Goblin extends GameCharacter {
    public Goblin(Vector2D position) {
        super("Goblin", 50, new GameStats(30, 5, 60, 30));
        setPosition(position);
        setDimensions(new Vector2D(100, 200));
        setSpriteName("goblin");
        setDescription(Dialogue.fromString("Goblin"));

        Goblin thisGoblin = this;

        getGameEventListenerManager().registerEventListener(new GameEventListener<SawAttackEvent>() {
            @Override
            protected void run(SawAttackEvent sawAttackEvent, Engine engine) {
                ExampleGameObject attacker = sawAttackEvent.getAttacker();
                if (!(attacker instanceof Goblin)) {
                    CombatManager.initiateAttack(thisGoblin, attacker, engine);
                }
            }
        });

        getGameEventListenerManager().registerEventListener(new GameEventListener<AttackedEvent>() {
            @Override
            protected void run(AttackedEvent attackedEvent, Engine engine) {
                ExampleGameObject attacker = attackedEvent.getAttacker();
                CombatManager.initiateAttack(thisGoblin, attacker, engine);
            }
        });
    }
}
