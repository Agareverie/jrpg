package com.jrpg.engine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.jrpg.engine.components.*;
import com.jrpg.engine.settings.*;
import com.jrpg.rendering.*;
import com.jrpg.rendering.graphics.Rectangle;
import com.jrpg.rendering.graphics.Sprite;
import com.jrpg.rendering.graphics.Text;
import com.jrpg.rendering.graphics.RectangleBorder;

public class Camera {
    private final Engine engine;
    private final GraphicsRenderer renderer;
    private Dialogue animatedDialogue;
    private final List<GameAnimation> gameAnimations = new ArrayList<>();
    private final Dimension dimensions = Dimensions.DEFAULT;

    private final Color defaultBackgroundColor = Color.LIGHT_GRAY;

    // For animations
    private double time = 0.0;
    private double animatedDialogueProgress = 0.0;
    private static final double globalAnimationSpeed = 1;
    private static final double animatedDialogueSpeed = 2;
    private final double cursorAnimationSpeed = 0.4;

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

    public void addGameAnimation(GameAnimation animation){
        if(!this.gameAnimations.contains(animation)) this.gameAnimations.add(animation);
    }

    public void removeGameAnimation(GameAnimation animation){
        if (this.gameAnimations.contains(animation)) {
            gameAnimations.remove(animation);
        }
    }

    private void addBackground() {
        Scene currentScene = engine.getCurrentScene();
        Vector2D backgroundDimensions = new Vector2D(dimensions.getWidth(), dimensions.getHeight() - DialogueBoxSettings.HEIGHT);

        if (currentScene.getBackgroundImageSpriteName() == null) {
            renderer.add(new Rectangle(Vector2D.zero().toCoordinate(), backgroundDimensions.toCoordinate(), defaultBackgroundColor));
            return;
        }

        renderer.add(new Sprite(
                Vector2D.zero().toCoordinate(),
                backgroundDimensions.toCoordinate(),
                SpriteLoader.getSprite(currentScene.getBackgroundImageSpriteName())
        ));
    }

    private void addGameObjectCursor() {
        GameObject currentGameObject = engine.getCurrentSelectedGameObject();

        renderer.add(Sprite.centered(
                currentGameObject.getPosition().add(0, 50 + 10 * Math.sin(cursorAnimationSpeed * time)).toCoordinate(),
                new Coordinate(50, 50), SpriteLoader.getSprite(SpriteNames.GAME_OBJECT_CURSOR)));
    }

    private void addGameObjects() {

        for (GameObject gameObject : engine.getCurrentSceneGameObjects()) {
            String spriteName = gameObject.getSpriteName();
            if(spriteName == null) {
                continue;
            }
            renderer.add(Sprite.centered(
                    gameObject.getPosition().toCoordinate(),
                    gameObject.getDimensions().toCoordinate(), SpriteLoader.getSprite(spriteName))
            );
        }
    }

    private void addUIObjects() {
        GameState gameState = engine.getGameState();
        addDialogue();

        if (gameState.isInActionMenu()) {
            addActionsMenu();
            return;
        }

        if (engine.getCurrentSelectedGameObject() != null && !gameState.isInDialogue()) {
            addGameObjectCursor();
        }
    }

    private void addDialogue() {
        GameState gameState = engine.getGameState();

        // render the box
        Vector2D dialogueBoxPosition = new Vector2D(0, this.dimensions.getHeight() - DialogueBoxSettings.HEIGHT);
        Vector2D dialogueBoxDimensions = new Vector2D(this.dimensions.getWidth(), DialogueBoxSettings.HEIGHT);
        renderer.add(new Rectangle(
                dialogueBoxPosition.toCoordinate(),
                dialogueBoxDimensions.toCoordinate(),
                DialogueBoxSettings.COLOR)
        );

        renderer.add(new RectangleBorder(
                dialogueBoxPosition.toCoordinate(),
                dialogueBoxDimensions.toCoordinate(),
                5, Color.BLACK)
        );

        // If there is currently an animated dialogue in progress render that
        if (animatedDialogue != null) {
            addPartialDialogueText(animatedDialogue, dialogueBoxPosition, (int) Math.round(animatedDialogueProgress));
            animatedDialogueProgress += animatedDialogueSpeed;
            return;
        }

        // Choose where to get the data to render the text from
        // (from current menu item or from current gameObject)
        Dialogue dialogue;
        if (gameState.isInActionMenu()) {
            GameAction currentAction = engine.getCurrentSelectedGameAction();
            Dialogue description = currentAction.getDescription();
            if (description == null) {
                return;
            }
            dialogue = description;
        } else {
            GameObject currentGameObject = engine.getCurrentSelectedGameObject();
            //empty scene
            if(currentGameObject == null) return;
            Dialogue description = currentGameObject.getDescription();
            if (description == null)
                return;
            dialogue = description;
        }

        addDialogueText(dialogue, dialogueBoxPosition);
    }

    private void addDialogueText(Dialogue dialogue, Vector2D dialogueBoxPosition) {
        addPartialDialogueText(dialogue, dialogueBoxPosition, dialogue.getLength());
    }

    private void addPartialDialogueText(Dialogue dialogue, Vector2D dialogueBoxPosition, int progress) {
        if (progress > dialogue.getLength()) {
            progress = dialogue.getLength();
        }

        int drawnCount = 0;
        int currentYOffset = DialogueBoxSettings.PADDING_Y;
        for (DialogueLine dialogueLine : dialogue.getLines()) {
            currentYOffset += dialogueLine.getMaxFontSize() + DialogueBoxSettings.LINE_SPACING;
            int currentXOffset = DialogueBoxSettings.PADDING_X;
            for (StyledText textFragment : dialogueLine.getTextFragments()) {
                String text = textFragment.text();
                int length = text.length();
                Font font = textFragment.font();

                // edge case for last string
                if (length + drawnCount > progress) {
                    String cutString = text.substring(0, progress - drawnCount);

                    renderer.add(new Text(
                            cutString,
                            dialogueBoxPosition.add(currentXOffset, currentYOffset).toCoordinate(),
                            textFragment.color(), font)
                    );
                    return;
                }

                renderer.add(new Text(
                        text,
                        dialogueBoxPosition.add(currentXOffset, currentYOffset).toCoordinate(),
                        textFragment.color(),
                        font
                ));

                FontMetrics metrics = new FontMetrics(font) {};
                currentXOffset += (int) metrics.getStringBounds(text, null).getWidth();
                drawnCount += length;
            }
        }
    }

    private void addActionsMenu() {
        GameInputHandler gameInputHandler = engine.getGameInputHandler();
        GameObject currentGameObject = gameInputHandler.getCurrentGameObject();
        
        // render menu box
        Vector2D actionsBoxPosition;
        float heightValue = (ActionsMenuSettings.FONT.getSize() + ActionsMenuSettings.PADDING_Y) * ActionsMenuSettings.MAX_ACTIONS_PER_PAGE + 2 * ActionsMenuSettings.PADDING_Y;
        Vector2D actionsBoxDimensions = new Vector2D(ActionsMenuSettings.WIDTH, heightValue);

        if(currentGameObject.getPosition().getX() + ActionsMenuSettings.WIDTH > Dimensions.WIDTH){
            actionsBoxPosition = currentGameObject.getPosition().sub(ActionsMenuSettings.WIDTH,0);
        }else{
            actionsBoxPosition = currentGameObject.getPosition();
        }
        renderer.add(new Rectangle(
                actionsBoxPosition.toCoordinate(),
                actionsBoxDimensions.toCoordinate(),
                ActionsMenuSettings.COLOR)
        );

        renderer.add(new RectangleBorder(
                actionsBoxPosition.toCoordinate(),
                actionsBoxDimensions.toCoordinate(),
                2,
                Color.BLACK)
        );

        // Add action names
        int selectedIndex = gameInputHandler.getCurrentGameActionIndex();
        List<GameAction> gameActions = engine.getCurrentGameActions();

        int currentYOffset = 0;

        // Pages
        int pageOffset = ActionsMenuSettings.MAX_ACTIONS_PER_PAGE * Math.floorDiv(selectedIndex, ActionsMenuSettings.MAX_ACTIONS_PER_PAGE);
        int currentPageActionsCount = Math.clamp(gameActions.size() - pageOffset, 0, ActionsMenuSettings.MAX_ACTIONS_PER_PAGE);

        for (int i = 0; i < currentPageActionsCount; i++) {
            currentYOffset += ActionsMenuSettings.FONT.getSize() + ActionsMenuSettings.PADDING_Y;

            Vector2D position = actionsBoxPosition.add(ActionsMenuSettings.PADDING_X, currentYOffset);
            int actualIndex = pageOffset + i;
            GameAction gameAction = gameActions.get(actualIndex);
            Font font = ActionsMenuSettings.FONT.deriveFont(actualIndex == selectedIndex ? Font.BOLD : Font.PLAIN);
            renderer.add(new Text(gameAction.getName(), position.toCoordinate(), Color.black, font));

            // Draw cursor
            if (actualIndex == selectedIndex) {
                renderer.add(Sprite.centered(
                        position.add(-20 + 10 * Math.sin(cursorAnimationSpeed * time), -font.getSize() / 4.).toCoordinate(),
                        new Coordinate(50, 50),
                        SpriteLoader.getSprite(SpriteNames.ACTIONS_MENU_CURSOR)
                ));
            }
        }
    }

    private void tickAnimations() {
        for (GameAnimation gameAnimation : gameAnimations) {
            gameAnimation.tick(time);
        }

        gameAnimations.removeIf(GameAnimation::isFinished);
    }

    public void clearAnimations() {
        gameAnimations.clear();
    }

    // main update loop
    public void update() {
        tickAnimations();

        // Clear old frame
        renderer.clear();

        addBackground();
        addGameObjects();
        addUIObjects();

        // Draw new frame
        renderer.render();

        time += globalAnimationSpeed;
    }
}
