package com.jrpg.engine;

import javax.swing.*;
import java.awt.*;
import com.jrpg.rendering.*;
import com.jrpg.rendering.graphics.CenteredRectangle;
import com.jrpg.rendering.graphics.CenteredSprite;

public class Camera {
    private Dimension dimensions = new Dimension(1200, 675);
    Engine engine;
    GraphicsRenderer renderer;

    //for animations
    double time = 0.;

    Camera(Engine engine, JFrame frame) {
        this.engine = engine;
        renderer = new GraphicsRenderer(frame, dimensions);
    }


    private void addGameObjects() {
        Scene currentScene = engine.getCurrentScene();

        for (GameObject gameObject : currentScene.getGameObjects()) {
            renderer.add(new CenteredSprite(gameObject.getPosition().toCoordinate(), gameObject.getDimensions().toCoordinate(), SpriteLoader.getSprite(gameObject.getSpriteName())));
        }
    }

    private void addUIObjects(){
        GameInputHandler gameInputHandler = engine.getGameInputHandler();
        GameObject currentGameObject = gameInputHandler.getCurrentGameObject();
        if(currentGameObject != null){
            renderer.add(new CenteredRectangle(currentGameObject.getPosition().add(0,50 + 10*Math.sin(time)).toCoordinate(), new Coordinate(25, 25), Color.red));
        }
    }

    // main update loop
    public void update() {
        // clear old frame
        renderer.clear();

        addGameObjects();
        addUIObjects();

        // draw new frame
        renderer.render();

        time++;
    }
}
