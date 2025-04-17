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
    private Engine engine;
    private GraphicsRenderer renderer;

    // TODO a settings class
    private Dimension dimensions = new Dimension(1200, 675);
    private int dialogueBoxHeight = 200;
    private int dialogueBoxPaddingX = 5;
    private int dialogueBoxPaddingY = 5;
    private int dialougeBoxLineSpacing = 1;
    private Font actionsMenuFont = new Font("Serif", Font.PLAIN, 20);
    private int actionsMenuMaxActions = 7; // max actions before scrolling (also determins height)
    private int actionsMenuPaddingX = 3;
    private int actionsMenuPaddingY = 3;
    private int actionsMenuWidth = 300;
    private Color dialogueBoxColor = new Color(.8f, .8f, 1.f);
    private Color actionsMenuColor = new Color(.8f, .8f, 1.f, .8f);
    private Color defaultBackgroundColor = Color.lightGray;

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
                defaultBackgroundColor));
    }

    private void addCursor() {
        GameInputHandler gameInputHandler = engine.getGameInputHandler();
        GameObject currentGameObject = gameInputHandler.getCurrentGameObject();

        if (currentGameObject != null && !engine.getGameState().isInActionMenu()) {
            renderer.add(Sprite.centered(
                    currentGameObject.getPosition().add(0, 50 + 10 * Math.sin(0.4 * time)).toCoordinate(),
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

        if (engine.getGameState().isInActionMenu()) {
            addActionsMenu();
        }

        addCursor();

    }

    private void addDialogueBox() {
        GameState gameState = engine.getGameState();

        // render the box
        Vector2D dialogueBoxPosition = new Vector2D(0, this.dimensions.getHeight() - this.dialogueBoxHeight);
        Vector2D dialogueBoxDimensions = new Vector2D(this.dimensions.getWidth(), dialogueBoxHeight);
        renderer.add(new Rectangle(dialogueBoxPosition.toCoordinate(), dialogueBoxDimensions.toCoordinate(),
                dialogueBoxColor));
        renderer.add(new UnfilledRectangle(dialogueBoxPosition.toCoordinate(),
                dialogueBoxDimensions.toCoordinate(), 5, Color.black));

        // choose where to get the data to render the text from
        // (from the current dialogue, from current menu item, from current gameObject)
        List<DialogueLine> dialogueLines;
        if (gameState.isInDialogue())
            dialogueLines = engine.getCurrentDialogue().getLines();
        else if (gameState.isInActionMenu()) {
            dialogueLines = new ArrayList<DialogueLine>();
            GameAction currentAction = engine.getCurrentSelectedAction();
            dialogueLines.add(currentAction.getDescription());
        } else {
            // TODO: create dialogueLines for displaying the selected gameObject info
            return;
        }
        ;

        // render dialogue text
        int currentYOffset = dialogueBoxPaddingY;
        for (DialogueLine dialogueLine : dialogueLines) {
            currentYOffset += dialogueLine.getMaxFontSize() + dialougeBoxLineSpacing;
            int currentXOffset = dialogueBoxPaddingX;
            for (StyledText textFragment : dialogueLine.getTextFragments()) {
                String text = textFragment.getText();
                Font font = textFragment.getFont();
                renderer.add(new Text(text, dialogueBoxPosition.add(currentXOffset, currentYOffset).toCoordinate(),
                        textFragment.getColor(), font));

                FontMetrics metrics = new FontMetrics(font) {

                };
                currentXOffset += metrics.getStringBounds(text, null).getWidth();
            }
        }
    }

    private void addActionsMenu() {
        GameInputHandler gameInputHandler = engine.getGameInputHandler();
        GameObject currentGameObject = gameInputHandler.getCurrentGameObject();
        Vector2D actionsBoxPosition = currentGameObject == null
                ? new Vector2D(dimensions.getWidth() / 2, dimensions.getHeight() / 2)
                : currentGameObject.getPosition();

        // render menu box
        Vector2D actionsBoxDimensions = new Vector2D(actionsMenuWidth,
                (actionsMenuFont.getSize() + actionsMenuPaddingY) * actionsMenuMaxActions + 2 * actionsMenuPaddingY);
        renderer.add(new Rectangle(actionsBoxPosition.toCoordinate(), actionsBoxDimensions.toCoordinate(),
                actionsMenuColor));
        renderer.add(new UnfilledRectangle(actionsBoxPosition.toCoordinate(), actionsBoxDimensions.toCoordinate(), 2,
                Color.black));

        // add action names
        int selectedIndex = gameInputHandler.getCurrentGameActionIndex();
        List<GameAction> gameActions = engine.getCurrentActions();

        int currentYOffset = 0;
        // pages
        int pageOffset = actionsMenuMaxActions * Math.floorDiv(selectedIndex, actionsMenuMaxActions);
        int currentPageActionsCount = Math.clamp(gameActions.size() - pageOffset, 0, actionsMenuMaxActions);
        for (int i = 0; i < currentPageActionsCount; i++) {
            currentYOffset += actionsMenuFont.getSize() + actionsMenuPaddingY;

            Vector2D position = actionsBoxPosition.add(actionsMenuPaddingX, currentYOffset);
            int actualIndex = pageOffset + i;
            GameAction gameAction = gameActions.get(actualIndex);
            Font font = actionsMenuFont.deriveFont(actualIndex == selectedIndex ? Font.BOLD : Font.PLAIN);
            renderer.add(new Text(gameAction.getName(), position.toCoordinate(), Color.black, font));

            // draw cursor
            if (actualIndex == selectedIndex) {
                renderer.add(Sprite.centered(
                        position.add(-20 + 10 * Math.sin(0.4 * time), -font.getSize() / 4.).toCoordinate(),
                        new Coordinate(50, 50), SpriteLoader.getSprite("arrow_right")));
            }
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
