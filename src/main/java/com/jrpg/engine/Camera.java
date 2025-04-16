package com.jrpg.engine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import com.jrpg.rendering.*;
import com.jrpg.rendering.graphics.Rectangle;
import com.jrpg.rendering.graphics.Sprite;
import com.jrpg.rendering.graphics.Text;
import com.jrpg.rendering.graphics.UnfilledRectangle;

public class Camera {
    private Dimension dimensions = new Dimension(1200, 675);
    //TODO a settings class
    private int dialogueBoxHeight = 200;
    private int dialogueBoxPadding = 5;
    private int dialougeBoxLineSpacing = 1;
    private Font actionsMenuFont = new Font("Serif", Font.PLAIN, 20);
    private Font infoFont = new Font("Serif", Font.PLAIN, 14);
    private Engine engine;
    private GraphicsRenderer renderer;

    private Color dialogueBoxColor = new Color(.8f, .8f, 1.f);
    private Color contextMenuColor = new Color(.8f, .8f, 1.f, .8f);

    // for animations
    private double time = 0.;

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

        if (engine.getGameState().isInMenu()) {
            renderer.add(Sprite.centered(
                    currentGameObject.getPosition()
                            .add(-20 + 10 * Math.sin(time), 10 + gameInputHandler.getCurrentGameActionIndex() * 15)
                            .toCoordinate(),
                    new Coordinate(50, 50), SpriteLoader.getSprite("arrow_right")));
        } else if (currentGameObject != null) {
            renderer.add(Sprite.centered(
                    currentGameObject.getPosition().add(0, 50 + 10 * Math.sin(time)).toCoordinate(),
                    new Coordinate(50, 50), SpriteLoader.getSprite("arrow")));
        }
    }

    private void addGameObjects() {
        Scene currentScene = engine.getCurrentScene();

        for (GameObject gameObject : currentScene.getGameObjects()) {
            renderer.add(Sprite.centered(gameObject.getPosition().toCoordinate(),
                    gameObject.getDimensions().toCoordinate(), SpriteLoader.getSprite(gameObject.getSpriteName())));
        }
    }

    private void addUIObjects() {
        // bottom dialogue box
        addDialogueBox();

        // TODO completely refactor how menu is rendered to be more modifiable
        // currently it works off of hardcoded constants
        // menu
        if (engine.getGameState().isInMenu())
            addMenu();

        addCursor();

    }

    private void addDialogueBox() {
        GameState gameState = engine.getGameState();

        //render the box
        Vector2D dialogueBoxPosition = new Vector2D(0, this.dimensions.getHeight() - this.dialogueBoxHeight);
        Vector2D dialogueBoxDimensions = new Vector2D(this.dimensions.getWidth(), dialogueBoxHeight);
        renderer.add(new Rectangle(dialogueBoxPosition.toCoordinate(),dialogueBoxDimensions.toCoordinate()
                , dialogueBoxColor));
        renderer.add(new UnfilledRectangle(dialogueBoxPosition.toCoordinate(),
        dialogueBoxDimensions.toCoordinate(), 5, Color.black));

        //choose where to get the data to render the text from
        //(from the current dialogue, from current menu item, from current gameObject)
        List<DialogueLine> dialogueLines;
        if(gameState.isInDialogue()) dialogueLines = engine.getCurrentDialogue().getLines();
        else if(gameState.isInMenu()) {
            dialogueLines = new ArrayList<DialogueLine>();
            GameAction currentAction = engine.getCurrentSelectedAction();
            dialogueLines.add(currentAction.getDescription());
        }else {
            //TODO: create dialogueLines for displaying the selected gameObject info
            return;
        };

        //render dialogue text
        int currentYOffset = dialogueBoxPadding;
        for (DialogueLine dialogueLine : dialogueLines) {
            currentYOffset += dialogueLine.getMaxFontSize() + dialougeBoxLineSpacing;
            int currentXOffset = dialogueBoxPadding;
            for (StyledText textFragment : dialogueLine.getTextFragments()) {
                String text = textFragment.getText();
                Font font = textFragment.getFont();
                renderer.add(new Text(text, dialogueBoxPosition.add(currentXOffset, currentYOffset).toCoordinate(), textFragment.getColor(), font));

                FontMetrics metrics = new FontMetrics(font) {
                    
                };
                currentXOffset += metrics.getStringBounds(text, null).getWidth();
            }
        }
    }

    private void addMenu() {
        GameInputHandler gameInputHandler = engine.getGameInputHandler();
        GameObject currentGameObject = gameInputHandler.getCurrentGameObject();
        Vector2D position = currentGameObject == null
                ? new Vector2D(dimensions.getWidth() / 2, dimensions.getHeight() / 2)
                : currentGameObject.getPosition();

        // render menu box
        renderer.add(new Rectangle(position.toCoordinate(), new Coordinate(200, 300), contextMenuColor));
        renderer.add(new UnfilledRectangle(position.toCoordinate(), new Coordinate(200, 300), 2, Color.black));

        // add action text
        // that aren't part of a gameObject by themselves
        int selectedIndex = gameInputHandler.getCurrentGameActionIndex();
        List<GameAction> gameActions = engine.getCurrentActions();
        for (int i = 0; i < gameActions.size(); i++) {
            GameAction gameAction = gameActions.get(i);
            renderer.add(new Text(gameAction.getName(), position.add(0, 15 + 15 * i).toCoordinate(), Color.black, actionsMenuFont.deriveFont(i == selectedIndex? Font.BOLD : Font.PLAIN)));
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
