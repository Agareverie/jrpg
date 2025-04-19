package com.jrpg.example_game;

import com.jrpg.engine.components.*;
import com.jrpg.example_game.events.AttackedEvent;
import com.jrpg.example_game.events.SawAttackEvent;

public class Goblin extends GameCharacter {
    public Goblin(Vector2D position){
        super("Goblin", 50, new GameStats(30, 5, 60, 30));
        setPosition(position);
        setDimensions(new Vector2D(100, 200));
        setSpriteName("goblin");
        setDescription(Dialogue.fromString("Goblin"));

        getGameEventListenerManager().registerEventListener(new GameEventListener("SawAttack", (gameEvent, engine) -> {
            ExampleGameObject attacker = ((SawAttackEvent) gameEvent).getAttacker();
            if(!(attacker instanceof Goblin)){
                CombatManager.initiateAttack(this, attacker, engine);
            }
        }));

        getGameEventListenerManager().registerEventListener(new GameEventListener("Attacked", (gameEvent, engine) -> {
            ExampleGameObject attacker = ((AttackedEvent) gameEvent).getAttacker();
            CombatManager.initiateAttack(this, attacker, engine);
        }));

        getGameEventListenerManager().registerEventListener(new GameEventListener("Death", (gameEvent, engine) -> {
            engine.getCurrentScene().remove(this);
        }));
    }
}
