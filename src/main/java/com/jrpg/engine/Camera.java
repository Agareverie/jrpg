package com.jrpg.engine;

import javax.swing.*;
import java.awt.*;
import com.jrpg.rendering.*;
import com.jrpg.rendering.graphics.Sprite;

public class Camera {
    private Dimension dimensions = new Dimension(1200, 675);
    Engine engine;
    GraphicsRenderer renderer;

    Camera(Engine engine, JFrame frame){
        this.engine = engine;
        renderer = new GraphicsRenderer(frame, dimensions);
    }

    private void addGameObjects(){
        Scene currentScene = engine.getCurrentScene();

        for(GameObject gameObject : currentScene.getGameObjects()){
            //coordinates offseted so that the position appear centered on the center of the sprite instead of the top right
            Coordinate offsettedCoordinates = gameObject.getPosition().sub(gameObject.getDimensions().scale(1/2., 1/2.)).toCoordinate();
            renderer.add(new Sprite(offsettedCoordinates, gameObject.getDimensions().toCoordinate(), SpriteLoader.getSprite(gameObject.getSpriteName())));
        }
    }

    private void addUIObjects(){
        //TODO
    }

    //main update loop
    public void update(){
        //clear old frame
        renderer.clear();

        addGameObjects();
        addUIObjects();
        
        //draw new frame
        renderer.render();
    }
}
