package com.jrpg;

import java.awt.*;
import java.util.Objects;

import javax.swing.*;

import com.jrpg.engine.*;
import com.jrpg.engine.components.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JRPG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Font font = new Font("Serif", Font.PLAIN, 24);

        GameAction nudgeLeft = new GameAction("Nudge Left", (Engine engine) -> {
            GameObject currentGameObject = engine.getGameInputHandler().getCurrentGameObject();
            currentGameObject.setPosition(currentGameObject.getPosition().add(-10, 0));
        });

        GameAction nudgeRight = new GameAction("Nudge Right", (Engine engine) -> {
            GameObject currentGameObject = engine.getGameInputHandler().getCurrentGameObject();
            currentGameObject.setPosition(currentGameObject.getPosition().add(10, 0));
        });

        GameAction nudgeUp = new GameAction("Nudge Up", (Engine engine) -> {
            GameObject currentGameObject = engine.getGameInputHandler().getCurrentGameObject();
            currentGameObject.setPosition(currentGameObject.getPosition().add(0, -10));
        });

        GameAction nudgeDown = new GameAction("Nudge Down", (Engine engine) -> {
            GameObject currentGameObject = engine.getGameInputHandler().getCurrentGameObject();
            currentGameObject.setPosition(currentGameObject.getPosition().add(0, 10));
        });

        GameAction talk = new GameAction("Talk", (Engine engine) -> {
            GameObject currentGameObject = engine.getCurrentSelectedGameObject();
            String name = Objects.equals(currentGameObject.getSpriteName(), "testSprite1") ? "Shiroko" : "Sensei";
            engine.enqueueDialogue(Dialogue.fromString("Hello, My Name Is " + name + "\nGood night", Color.black, font));
        });

        Scene scene = new Scene();

        for (int i = 0; i < 10; i++) {
            Vector2D position = new Vector2D(Math.floor(Math.random() * 1200 + 1), Math.floor(Math.random() * 475 + 1));
            GameObject gameObject = new GameObject(position, new Vector2D(70, 70), i % 2 == 0 ? "testSprite1" : "testSprite2");
            
            gameObject.setSelectable(Math.random() < 0.5);

            if (gameObject.isSelectable()) {
                gameObject.addGameAction(nudgeLeft);
                gameObject.addGameAction(nudgeRight);
                gameObject.addGameAction(nudgeUp);
                gameObject.addGameAction(nudgeDown);
                gameObject.addGameAction(talk);

                if(Math.random() < .5) {
                    gameObject.addGameAction(nudgeLeft);
                    gameObject.addGameAction(nudgeRight);
                    gameObject.addGameAction(nudgeUp);
                    gameObject.addGameAction(nudgeDown);
                    gameObject.addGameAction(talk);
                }
            } else gameObject.setSpriteName("default");

            scene.add(gameObject);
        }

        scene.setBackgroundImageSpriteName("testBackground");

        Engine engine = new Engine(frame, scene);

        DialogueLine testLine1 = new DialogueLine();
        testLine1.add("test1 ", Color.black, new Font("Serif", Font.PLAIN, 20));
        testLine1.add("test2 ", Color.red, new Font("Serif", Font.PLAIN, 25));
        testLine1.add("test3", Color.green, new Font("Arial", Font.BOLD, 25));

        DialogueLine testLine2 = new DialogueLine();
        testLine2.add("test4 ", Color.black, new Font("Serif", Font.PLAIN, 20));
        testLine2.add("test5 ", Color.red, new Font("Serif", Font.PLAIN, 25));
        testLine2.add("test6", Color.green, new Font("Arial", Font.BOLD, 25));

        Dialogue dialogue1 = new Dialogue();
        dialogue1.addLine(testLine1);
        dialogue1.addLine(testLine2);
        Dialogue dialogue2 = new Dialogue();
        dialogue2.addLine(testLine2);
        dialogue2.addLine(testLine1);

        engine.enqueueDialogue(dialogue1);
        engine.enqueueDialogue(dialogue2);

        engine.loop();
    }
}