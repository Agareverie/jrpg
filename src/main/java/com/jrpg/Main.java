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
        Font font = new Font("Serif", Font.PLAIN, 24);
        ArrayList<Scene> scenes = new ArrayList<Scene>();

        GameAction nudgeLeft = new GameAction("Nudge Left", new DialogueLine("Nudges the current object left", Color.black, font), (Engine engine) -> {
            GameObject currentGameObject = engine.getGameInputHandler().getCurrentGameObject();
            currentGameObject.setPosition(currentGameObject.getPosition().add(-10, 0));
        });
        GameAction nudgeRight = new GameAction("Nudge Right", new DialogueLine("Nudges the current object right", Color.black, font), (Engine engine) -> {
            GameObject currentGameObject = engine.getGameInputHandler().getCurrentGameObject();
            currentGameObject.setPosition(currentGameObject.getPosition().add(10, 0));
        });
        GameAction nudgeUp = new GameAction("Nudge Up", new DialogueLine("Nudges the current object up", Color.black, font), (Engine engine) -> {
            GameObject currentGameObject = engine.getGameInputHandler().getCurrentGameObject();
            currentGameObject.setPosition(currentGameObject.getPosition().add(0, -10));
        });
        GameAction nudgeDown = new GameAction("Nudge Down", new DialogueLine("Nudges the current object down", Color.black, font), (Engine engine) -> {
            GameObject currentGameObject = engine.getGameInputHandler().getCurrentGameObject();
            currentGameObject.setPosition(currentGameObject.getPosition().add(0, 10));
        });
        GameAction talk = new GameAction("Talk", new DialogueLine("Talks", Color.black, font), (Engine engine) -> {
            GameObject currentGameObject = engine.getCurrentSelectedGameObject();
            String name = currentGameObject.getSpriteName() == "testSprite1" ? "Shiroko" : "Sensei";
            engine.enqueueDialogue(Dialogue.fromString("Hello, My Name Is " + name + "\nGood night", Color.black, font));
        });

        Scene scene = new Scene();

        for (int i = 0; i < 10; i++) {
            GameObject gameObject = new GameObject(new Vector2D(170 + 80 * i, 150), new Vector2D(70, 70), i % 2 == 0? "testSprite1" : "testSprite2");
            gameObject.addGameAction(nudgeLeft);
            gameObject.addGameAction(nudgeRight);
            gameObject.addGameAction(nudgeUp);
            gameObject.addGameAction(nudgeDown);
            gameObject.addGameAction(talk);
            scene.add(gameObject);
        }

        scenes.add(scene);

        Engine engine = new Engine(frame, scenes);

        DialogueLine testLine1 = new DialogueLine();
        testLine1.add("test1 ", Color.black, new Font("Serif", Font.PLAIN, 20));
        testLine1.add("test2 ", Color.red, new Font("Serif", Font.PLAIN, 25));
        testLine1.add("test3", Color.green, new Font("Arial", Font.BOLD, 25));

        DialogueLine testLine2 = new DialogueLine();
        testLine2.add("test1 ", Color.black, new Font("Serif", Font.PLAIN, 20));
        testLine2.add("test2 ", Color.red, new Font("Serif", Font.PLAIN, 25));
        testLine2.add("test3", Color.green, new Font("Arial", Font.BOLD, 25));
        
        Dialogue dialogue1 = new Dialogue();
        dialogue1.addLine(testLine1);
        dialogue1.addLine(testLine2);
        Dialogue dialogue2 = new Dialogue();
        dialogue2.addLine(testLine2);
        dialogue2.addLine(testLine1);

        engine.enqueueDialogue(dialogue1);
        engine.enqueueDialogue(dialogue2);

        while (true) {
            engine.update();
            try {
                Thread.sleep(Math.round(1000 / 60));
            } catch (InterruptedException e) {

            }
        }
    }
}