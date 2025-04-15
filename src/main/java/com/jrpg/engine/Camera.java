package com.jrpg.engine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import com.jrpg.rendering.*;
import com.jrpg.rendering.graphics.CenteredRectangle;
import com.jrpg.rendering.graphics.CenteredSprite;
import com.jrpg.rendering.graphics.Rectangle;
import com.jrpg.rendering.graphics.Text;
import com.jrpg.rendering.graphics.UnfilledRectangle;

public class Camera {
    private Dimension dimensions = new Dimension(1200, 675);
    private int dialogueBoxHeight = 200;
    private Font font = new Font("Serif", Font.PLAIN, 20);
    private Font fontBolded = new Font("Serif", Font.BOLD, 20);
    Engine engine;
    GraphicsRenderer renderer;

    // for animations
    double time = 0.;

    Camera(Engine engine, JFrame frame) {
        this.engine = engine;
        renderer = new GraphicsRenderer(frame, dimensions);
    }

    private void addBackground() {
        // TODO attach background sprite to scenes
        renderer.add(new Rectangle(new Coordinate(0, 0),
                new Coordinate((int) dimensions.getWidth(), (int) dimensions.getHeight() - dialogueBoxHeight),
                Color.lightGray));
    }

    private void addCursor() {
        GameInputHandler gameInputHandler = engine.getGameInputHandler();
        GameObject currentGameObject = gameInputHandler.getCurrentGameObject();

        if (gameInputHandler.isInMenu()) {
            renderer.add(new CenteredSprite(
                    currentGameObject.getPosition()
                            .add(-20 + 10 * Math.sin(time),10 + gameInputHandler.getCurrentMenuIndex() * 15).toCoordinate(),
                    new Coordinate(50, 50), SpriteLoader.getSprite("arrow_right")));
        } else if (currentGameObject != null) {
            renderer.add(new CenteredSprite(
                    currentGameObject.getPosition().add(0, 50 + 10 * Math.sin(time)).toCoordinate(),
                    new Coordinate(50, 50), SpriteLoader.getSprite("arrow")));
        }
    }

    private void addGameObjects() {
        Scene currentScene = engine.getCurrentScene();

        for (GameObject gameObject : currentScene.getGameObjects()) {
            renderer.add(new CenteredSprite(gameObject.getPosition().toCoordinate(),
                    gameObject.getDimensions().toCoordinate(), SpriteLoader.getSprite(gameObject.getSpriteName())));
        }
    }

    private void addUIObjects() {
        GameInputHandler gameInputHandler = engine.getGameInputHandler();

        // bottom dialogue box
        renderer.add(new Rectangle(new Coordinate(0, (int) this.dimensions.getHeight() - this.dialogueBoxHeight),
                new Coordinate((int) this.dimensions.getWidth(), dialogueBoxHeight), new Color(.8f, .8f, 1.f)));

        //TODO completely refactor how menu is rendered to be more modifiable
        //currently it works off of hardcoded constants
        // menu
        if (gameInputHandler.isInMenu())
            addMenu();

        addCursor();
        

    }

    private void addMenu() {
        GameInputHandler gameInputHandler = engine.getGameInputHandler();
        GameObject currentGameObject = gameInputHandler.getCurrentGameObject();
        Vector2D position = currentGameObject == null
                ? new Vector2D(dimensions.getWidth() / 2, dimensions.getHeight() / 2)
                : currentGameObject.getPosition();

        // render menu box
        renderer.add(new Rectangle(position.toCoordinate(), new Coordinate(200, 300), new Color(.8f, .8f, 1.f, .8f)));
        renderer.add(new UnfilledRectangle(position.toCoordinate(), new Coordinate(200, 300), 2, Color.black));

        // add action text
        // TODO: this needs to pull the actions from elsewhere to support general actions
        // that aren't part of a gameObject by themselves
        int selectedIndex = gameInputHandler.getCurrentMenuIndex();
        ArrayList<GameAction> gameActions = currentGameObject.getGameActions();
        for (int i = 0; i < gameActions.size(); i++) {
            GameAction gameAction = gameActions.get(i);
            renderer.add(new Text(gameAction.getName(), position.add(0, 15 + 15 * i).toCoordinate(), i == selectedIndex? fontBolded : font));
        }
    }

    // main update loop
    public void update() {
        // clear old frame
        renderer.clear();

        addBackground();
        addGameObjects();
        addUIObjects();

        // draw new frame
        renderer.render();

        time++;
    }
}
