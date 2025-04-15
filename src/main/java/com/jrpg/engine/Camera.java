package com.jrpg.engine;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

import com.jrpg.dimensions.Dimensions;
import com.jrpg.rendering.*;
import com.jrpg.rendering.graphics.Rectangle;
import com.jrpg.rendering.graphics.Sprite;

public class Camera {
    private static final Dimension dimensions = Dimensions.DEFAULT;
    private final Engine engine;
    private final GraphicsRenderer renderer;
    private double time = 0.0;

    Camera(Engine engine, JFrame frame) {
        this.engine = engine;
        renderer = new GraphicsRenderer(frame, dimensions);
    }

    private void addGameObjects() {
        Scene currentScene = engine.getCurrentScene();

        for (GameObject gameObject : currentScene.getGameObjects()) {
            renderer.add(Sprite.centered(gameObject.getPosition().toCoordinate(), gameObject.getDimensions().toCoordinate(), SpriteLoader.getSprite(gameObject.getSpriteName())));
        }
    }

    private void addUIObjects() {
        GameInputHandler gameInputHandler = engine.getGameInputHandler();
        Optional<GameObject> currentGameObject = gameInputHandler.getCurrentGameObject();
        currentGameObject.ifPresent(gameObject -> {
            Coordinate position = gameObject.getPosition().add(0, 50 + (10 * Math.sin(time))).toCoordinate();
            renderer.add(Rectangle.centered(position, new Coordinate(25, 25), Color.RED));
        });
    }

    // Main update loop
    public void update() {
        // Clear old frame
        renderer.clear();

        addGameObjects();
        addUIObjects();

        // Draw new frame
        renderer.render();
        ++time;
    }
}
