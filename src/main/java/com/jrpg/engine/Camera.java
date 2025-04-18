package com.jrpg.engine;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import com.jrpg.rendering.*;
import com.jrpg.rendering.graphics.Rectangle;
import com.jrpg.rendering.graphics.Sprite;
import com.jrpg.rendering.graphics.Text;
import com.jrpg.rendering.graphics.UnfilledRectangle;

public class Camera {
    private Engine engine;
    private GraphicsRenderer renderer;
    private Dialogue animatedDialogue;

    // TODO a settings class
    private Dimension dimensions = new Dimension(1200, 675);
    private double animatedDialogueSpeed = 2;
    private double cursorAnimationSpeed = 0.4;
    private String gameObjectCursorSpriteName = "arrow";
    private String actionsMenuCursorSpriteName = "arrow_right";
    private int dialogueBoxHeight = 200;
    private int dialogueBoxPaddingX = 5;
    private int dialogueBoxPaddingY = 5;
    private int dialougeBoxLineSpacing = 1;
    private Font actionsMenuFont = new Font("Serif", Font.PLAIN, 20);
    private int actionsMenuMaxActionsPerPage = 7; // max actions before switching pages (also determins height)
    private int actionsMenuPaddingX = 3;
    private int actionsMenuPaddingY = 3;
    private int actionsMenuWidth = 300;
    private Color dialogueBoxColor = new Color(.8f, .8f, 1.f);
    private Color actionsMenuColor = new Color(.8f, .8f, 1.f, .8f);
    private Color defaultBackgroundColor = Color.lightGray;

    // for animations
    private double time = 0.;
    private double animatedDialogueProgress = 0;

    public void setAnimatedDialogue(Dialogue animatedDialogue) {
        this.animatedDialogue = animatedDialogue;
        this.animatedDialogueProgress = 0;
    }

    Camera(Engine engine, JFrame frame) {
        this.engine = engine;
        renderer = new GraphicsRenderer(frame, dimensions);
    }

    public boolean isAnimatedDialogueFinished() {
        if (animatedDialogue == null)
            return true;
        return this.animatedDialogueProgress >= animatedDialogue.getLength();
    }

    public void skipAnimatedDialogue() {
        if (animatedDialogue == null)
            return;
        this.animatedDialogueProgress = animatedDialogue.getLength();
    }

    private void addBackground() {
        // TODO attach background sprite to scenes
        renderer.add(new Rectangle(new Coordinate(0, 0),
                new Coordinate((int) dimensions.getWidth(), (int) dimensions.getHeight() - dialogueBoxHeight),
                defaultBackgroundColor));
    }

    private void addGameObjectCursor() {
        GameObject currentGameObject = engine.getCurrentSelectedGameObject();

        renderer.add(Sprite.centered(
                currentGameObject.getPosition().add(0, 50 + 10 * Math.sin(cursorAnimationSpeed * time)).toCoordinate(),
                new Coordinate(50, 50), SpriteLoader.getSprite(gameObjectCursorSpriteName)));

    }

    private void addGameObjects() {

        for (GameObject gameObject : engine.getCurrentSceneGameObjects()) {
            renderer.add(Sprite.centered(gameObject.getPosition().toCoordinate(),
                    gameObject.getDimensions().toCoordinate(), SpriteLoader.getSprite(gameObject.getSpriteName())));
        }
    }

    private void addUIObjects() {
        GameState gameState = engine.getGameState();
        addDialogue();

        if (gameState.isInActionMenu()) {
            addActionsMenu();
        }

        else if (engine.getCurrentSelectedGameObject() != null && !gameState.isInDialogue()) {
            addGameObjectCursor();
        }

    }

    private void addDialogue() {
        GameState gameState = engine.getGameState();

        // render the box
        Vector2D dialogueBoxPosition = new Vector2D(0, this.dimensions.getHeight() - this.dialogueBoxHeight);
        Vector2D dialogueBoxDimensions = new Vector2D(this.dimensions.getWidth(), dialogueBoxHeight);
        renderer.add(new Rectangle(dialogueBoxPosition.toCoordinate(), dialogueBoxDimensions.toCoordinate(),
                dialogueBoxColor));
        renderer.add(new UnfilledRectangle(dialogueBoxPosition.toCoordinate(),
                dialogueBoxDimensions.toCoordinate(), 5, Color.black));

        // if there is currently an animated dialogue in progress render that
        if (animatedDialogue != null) {
            addDialogueText(animatedDialogue, dialogueBoxPosition, (int) Math.round(animatedDialogueProgress));
            animatedDialogueProgress += animatedDialogueSpeed;
            return;
        }
        // choose where to get the data to render the text from
        // (from current menu item or from current gameObject)
        Dialogue dialogue;
        if (gameState.isInActionMenu()) {
            GameAction currentAction = engine.getCurrentSelectedGameAction();
            Dialogue description = currentAction.getDescription();
            if (description == null)
                return;
            dialogue = description;
        } else {
            GameObject currentGameObject = engine.getCurrentSelectedGameObject();
            Dialogue description = currentGameObject.getDescription();
            if (description == null)
                return;
            dialogue = description;
        }

        addDialogueText(dialogue, dialogueBoxPosition);

    }

    private void addDialogueText(Dialogue dialogue, Vector2D dialogueBoxPosition) {
        addDialogueText(dialogue, dialogueBoxPosition, dialogue.getLength());
    }

    // im not sure what to name this
    // renders (a part of) the text onto the screen
    // works like subString(0, progress + 1)
    private void addDialogueText(Dialogue dialogue, Vector2D dialogueBoxPosition, int progress) {
        if (progress > dialogue.getLength()) {
            progress = dialogue.getLength();
        }

        int drawnCount = 0;
        int currentYOffset = dialogueBoxPaddingY;
        for (DialogueLine dialogueLine : dialogue.getLines()) {
            currentYOffset += dialogueLine.getMaxFontSize() + dialougeBoxLineSpacing;
            int currentXOffset = dialogueBoxPaddingX;
            for (StyledText textFragment : dialogueLine.getTextFragments()) {
                String text = textFragment.getText();
                int length = text.length();
                Font font = textFragment.getFont();

                // edge case for last string
                if (length + drawnCount > progress) {
                    String cuttedString = text.substring(0, progress - drawnCount);

                    renderer.add(new Text(cuttedString,
                            dialogueBoxPosition.add(currentXOffset, currentYOffset).toCoordinate(),
                            textFragment.getColor(), font));
                    return;
                }

                renderer.add(new Text(text, dialogueBoxPosition.add(currentXOffset, currentYOffset).toCoordinate(),
                        textFragment.getColor(), font));

                FontMetrics metrics = new FontMetrics(font) {

                };
                currentXOffset += metrics.getStringBounds(text, null).getWidth();
                drawnCount += length;
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
                (actionsMenuFont.getSize() + actionsMenuPaddingY) * actionsMenuMaxActionsPerPage
                        + 2 * actionsMenuPaddingY);
        renderer.add(new Rectangle(actionsBoxPosition.toCoordinate(), actionsBoxDimensions.toCoordinate(),
                actionsMenuColor));
        renderer.add(new UnfilledRectangle(actionsBoxPosition.toCoordinate(), actionsBoxDimensions.toCoordinate(), 2,
                Color.black));

        // add action names
        int selectedIndex = gameInputHandler.getCurrentGameActionIndex();
        List<GameAction> gameActions = engine.getCurrentGameActions();

        int currentYOffset = 0;
        // pages
        int pageOffset = actionsMenuMaxActionsPerPage * Math.floorDiv(selectedIndex, actionsMenuMaxActionsPerPage);
        int currentPageActionsCount = Math.clamp(gameActions.size() - pageOffset, 0, actionsMenuMaxActionsPerPage);
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
                        position.add(-20 + 10 * Math.sin(cursorAnimationSpeed * time), -font.getSize() / 4.)
                                .toCoordinate(),
                        new Coordinate(50, 50), SpriteLoader.getSprite(actionsMenuCursorSpriteName)));
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
