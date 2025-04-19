package com.jrpg.example_game;

import com.jrpg.engine.Engine;
import com.jrpg.engine.components.*;
import com.jrpg.example_game.events.AttackedEvent;
import com.jrpg.example_game.events.DeathEvent;
import com.jrpg.example_game.events.SawAttackEvent;

public class CombatManager {
    public static void initiateAttack(ExampleGameObject attacker, ExampleGameObject defender, Engine engine) {
        ExampleScene currentScene = (ExampleScene) engine.getCurrentScene();
        GameStats attackerStats = attacker.getEffectiveStats();
        GameStats defenderStats = defender.getEffectiveStats();

        int effectiveAttack = Math.max(0, attackerStats.attack() - defenderStats.defense());
        int effectiveAccuracy = Math.max(0, attackerStats.accuracy() - defenderStats.evasion());

        engine.enqueueDialogue(Dialogue.fromString(attacker.getName() + " attacks " + defender.getName()));
        if (Random.randomChance((double) effectiveAccuracy / 100.0)) {
            int health = defender.getHealth();
            health -= effectiveAttack;
            engine.enqueueDialogue(Dialogue.fromString(attacker.getName() + " hits " + defender.getName() + " for " + effectiveAttack + " damage"));
            
            defender.setHealth(health);

            if (health <= 0) {
                defender.getGameEventListenerManager().notifyEvent(new DeathEvent(), engine);
            } else {
                defender.getGameEventListenerManager().notifyEvent(new AttackedEvent(attacker), engine);
            }
        } else {
            engine.enqueueDialogue(Dialogue.fromString(attacker.getName() + " missed"));
        }

        currentScene.getGameObjects()
                .stream()
                .filter(gameObject -> gameObject instanceof ExampleGameObject)
                .map(gameObject -> (ExampleGameObject) gameObject)
                .filter(gameObject -> gameObject != defender)
                .forEach(gameObject -> {
                    gameObject.getGameEventListenerManager().notifyEvent(new SawAttackEvent(attacker, defender), engine);
                });
    }
}
