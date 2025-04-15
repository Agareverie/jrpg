package com.jrpg;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import com.jrpg.engine.*;
import com.jrpg.rendering.*;
import com.jrpg.rendering.graphics.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JRPG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ArrayList<Scene> scenes = new ArrayList<Scene>();

        GameAction nudgeLeft = new GameAction("Nudge Left", "Nudges the current object left", (Engine engine) -> {
            GameObject currentGameObject = engine.getGameInputHandler().getCurrentGameObject();
            currentGameObject.setPosition(currentGameObject.getPosition().add(-10, 0));
        });
        GameAction nudgeRight = new GameAction("Nudge Right", "Nudges the current object right", (Engine engine) -> {
            GameObject currentGameObject = engine.getGameInputHandler().getCurrentGameObject();
            currentGameObject.setPosition(currentGameObject.getPosition().add(10, 0));
        });
        GameAction nudgeUp = new GameAction("Nudge Up", "Nudges the current object up", (Engine engine) -> {
            GameObject currentGameObject = engine.getGameInputHandler().getCurrentGameObject();
            currentGameObject.setPosition(currentGameObject.getPosition().add(0, -10));
        });
        GameAction nudgeDown = new GameAction("Nudge Down", "Nudges the current object down", (Engine engine) -> {
            GameObject currentGameObject = engine.getGameInputHandler().getCurrentGameObject();
            currentGameObject.setPosition(currentGameObject.getPosition().add(0, 10));
        });

        Scene scene = new Scene();

        for (int i = 0; i < 10; i++) {
            GameObject gameObject = new GameObject(new Vector2D(170 + 80 * i, 150), new Vector2D(70, 70), i % 2 == 0? "testSprite1" : "testSprite2");
            gameObject.addGameAction(nudgeLeft);
            gameObject.addGameAction(nudgeRight);
            gameObject.addGameAction(nudgeUp);
            gameObject.addGameAction(nudgeDown);
            scene.add(gameObject);
        }

        scenes.add(scene);

        Engine engine = new Engine(frame, scenes);

        while (true) {
            engine.update();
            try {
                Thread.sleep(Math.round(1000 / 60));
            } catch (InterruptedException e) {

            }
        }
    }
}