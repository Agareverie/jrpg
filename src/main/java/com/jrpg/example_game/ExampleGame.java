package com.jrpg.example_game;

import javax.swing.JFrame;
import java.awt.Font;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;

import com.jrpg.engine.Engine;
import com.jrpg.engine.components.Dialogue;
import com.jrpg.engine.components.DialogueLine;
import com.jrpg.engine.components.GameAction;
import com.jrpg.engine.components.GameObject;
import com.jrpg.engine.components.Scene;
import com.jrpg.engine.components.Vector2D;
import com.jrpg.engine.settings.Dimensions;
import com.jrpg.engine.settings.AcceptKeyMaps;
import com.jrpg.engine.settings.DialogueBoxSettings;
import com.jrpg.engine.settings.DirectionalKeyMaps;
import com.jrpg.example_game.events.AttackedEvent;
import com.jrpg.example_game.events.DeathEvent;
import com.jrpg.example_game.events.SawAttackEvent;

public class ExampleGame {
    private static Engine engine;
    private static Player player;
    private static List<GameObject> globalGameObjects = new ArrayList<GameObject>();

    public static void initialize(JFrame frame) {
        if (engine != null)
            return;
        engine = new Engine(frame, new Scene());
        restart();
        engine.loop();
    }

    public static void restart() {
        globalGameObjects.clear();

        setUpPlayer();
        setUpGlobalGameObjects();
        engine.reset(setUpStarterScene());
        setUpConditionalActions();
    }

    private static ExampleScene setUpStarterScene() {
        ExampleScene weaponsShop = new ExampleScene("Shop");

        // shopkeeper
        GameCharacter shopkeeper = new GameCharacter("Shopkeeper", 50, new GameStats(20, 10, 80, 20));
        shopkeeper.setDescription(Dialogue.fromString("The shopkeeper"));
        shopkeeper.setSpriteName("human");
        shopkeeper.setPosition(new Vector2D(700, 200));
        shopkeeper.setDimensions(new Vector2D(120, 240));

        shopkeeper.getGameEventListenerManager().registerEventListener(new GameEventListener<SawAttackEvent>() {
            @Override
            protected void run(SawAttackEvent sawAttackEvent, Engine engine) {
                engine.enqueueDialogue(Dialogue.fromString(
                        "Shopkeeper:\nWhy are you attacking my " + (sawAttackEvent.getDefender().getName() + "?")));
            }
        });

        shopkeeper.getGameEventListenerManager().registerEventListener(new GameEventListener<AttackedEvent>() {
            @Override
            protected void run(AttackedEvent attackedEvent, Engine engine) {
                engine.enqueueDialogue(Dialogue.fromString("""
                        Shopkeeper:
                        Ouch!
                        """));
            }
        });

        // buy actions
        GameAction buySword = new GameAction("Buy Sword", Dialogue.fromString("Buy a sword, increases Attack."),
                (engine) -> {
                    player.addItem(new Item("Sword", new GameStats(30, 0, 0, 0)));
                    engine.enqueueDialogue(Dialogue.fromString("You bought a sword!"));
                });
        buySword.setClosesMenu(false);

        GameAction buyShield = new GameAction("Buy Shield", Dialogue.fromString("Buy a shield, increases Defense."),
                (engine) -> {
                    player.addItem(new Item("Shield", new GameStats(0, 30, 0, 0)));
                    engine.enqueueDialogue(Dialogue.fromString("You bought a shield!"));
                });
        buyShield.setClosesMenu(false);

        GameAction buyGlasses = new GameAction("Buy Glasses",
                Dialogue.fromString("Buy a pair of glasses, makes you see better."), (engine) -> {
                    player.addItem(new Item("Glasses", new GameStats(0, 0, 30, 0)));
                    engine.enqueueDialogue(Dialogue.fromString("You bought a pair of glasses!"));
                });
        buyGlasses.setClosesMenu(false);

        GameAction buyBoots = new GameAction("Buy Boots",
                Dialogue.fromString("Buy a pair of boots, made specifically for dodging."), (engine) -> {
                    player.addItem(new Item("Boots", new GameStats(0, 0, 0, 30)));
                    engine.enqueueDialogue(Dialogue.fromString("You bought some boots!"));
                });
        buyBoots.setClosesMenu(false);


        GameAction talk = new GameAction("Talk", Dialogue.empty(), (engine) -> {
            engine.enqueueDialogue(Dialogue.fromString("""
                    Shopkeeper:
                    Welcome, adventurer, to my humble weapons shop.
                    Please buy anything that catches your eye.
                    """));
        });

        shopkeeper.addGameAction(buySword);
        shopkeeper.addGameAction(buyShield);
        shopkeeper.addGameAction(buyGlasses);
        shopkeeper.addGameAction(buyBoots);
        shopkeeper.addGameAction(talk);

        // furniture (for parity)
        ExampleGameObject shelves = new ExampleGameObject("Shelves", 50, new GameStats(0, 60, 0, -40));
        shelves.setDescription(Dialogue.fromString("Shelves"));
        shelves.setSpriteName("shelves");
        shelves.setPosition(new Vector2D(200, 100));
        shelves.setDimensions(new Vector2D(200, 100));

        ExampleGameObject weaponRack = new ExampleGameObject("Weapons Rack", 50, new GameStats(0, 60, 0, -40));
        weaponRack.setDescription(Dialogue.fromString("Weapons Rack"));
        weaponRack.setSpriteName("weapon_rack");
        weaponRack.setPosition(new Vector2D(400, 350));
        weaponRack.setDimensions(new Vector2D(400, 400));

        ExampleGameObject box = new ExampleGameObject("Box", 25, new GameStats(0, 10, 0, -40));
        box.setDescription(Dialogue.fromString("Box\nseems to be in bad condition"));
        box.setSpriteName("box");
        box.setPosition(new Vector2D(100, 350));
        box.setDimensions(new Vector2D(400, 400));

        // Teleporter
        Teleporter door = new Teleporter(new Vector2D(900, 150), new Vector2D(400, 400), "door");
        door.setDescription(Dialogue.fromString("Door"));
        door.addDestination(weaponsShop);
        door.addDestination(setUpForestScene(door));
        door.addDestination(setUpDemoScene(door));

        weaponsShop.add(shopkeeper);
        weaponsShop.add(shelves);
        weaponsShop.add(weaponRack);
        weaponsShop.add(box);
        weaponsShop.add(door);

        weaponsShop.addMany(globalGameObjects);

        return weaponsShop;
    }

    private static ExampleScene setUpForestScene(Teleporter teleporter) {
        ExampleScene forest = new ExampleScene("Forest", "forest_background");

        forest.add(new Goblin("Goblin 1", new Vector2D(200, 250)));
        forest.add(new Goblin("Goblin 2", new Vector2D(400, 250)));
        forest.add(new Goblin("Goblin 3", new Vector2D(600, 250)));
        forest.add(teleporter);

        forest.addMany(globalGameObjects);
        return forest;
    }

    private static ExampleScene setUpDemoScene(Teleporter teleporter){
        ExampleScene demoScene = new ExampleScene("other demos");
        // animation demonstration
        // I left it pretty open so you should be able to control it however you want
        // ,but it's basically just a timer that is allowed to control anything (which
        // it's supposed to control animations)
        // you could have it make a game object rapidly change sprites to create
        // animations and stuff like that
        GameObject animationTarget = new GameObject(new Vector2D(300, 300), new Vector2D(400, 400), "shiroko");
        animationTarget.setDescription(Dialogue.fromString("Animation Demo"));
        ShrinkAndGrow ShrinkAndGrow = new ShrinkAndGrow(animationTarget, .25);

        animationTarget.addGameAction(new GameAction("Start Animation", Dialogue.empty(), (engine) -> {
            engine.getCamera().addGameAnimation(ShrinkAndGrow);
            ShrinkAndGrow.setActive(true);
        }));

        animationTarget.addGameAction(new GameAction("Stop Animation", Dialogue.empty(), (engine) -> {
            ShrinkAndGrow.setActive(false);
        }));

        demoScene.add(animationTarget);
        demoScene.add(teleporter);

        demoScene.addMany(globalGameObjects);

        return demoScene;

    }

    private static ExampleScene setUpDefeatScene() {
        ExampleScene defeatScene = new ExampleScene("Defeat");
        defeatScene.setBackgroundImageSpriteName("death_background");
        GameObject restartButton = new GameObject(new Vector2D(Dimensions.WIDTH/2, 225), Vector2D.zero(), null);
        restartButton.addGameAction(new GameAction("Restart", Dialogue.fromString("Restart"), (engine) -> {restart();}));
        
        defeatScene.add(restartButton);
        return defeatScene;
    }

    private static void setUpConditionalActions() {
        // example conditional action
        GameAction attack = new GameAction("Attack", Dialogue.fromString("Attack!!!"),
                (engine) -> {
                    CombatManager.initiateAttack(player, (ExampleGameObject) engine.getCurrentSelectedGameObject(),
                            engine);
                },
                (gameObject) -> {
                    if (gameObject instanceof ExampleGameObject exampleGameObject) {
                        return !exampleGameObject.hasTag("Not Attackable");
                    }
                    return false;
                });

        engine.addGeneralGameAction(attack);
    }

    private static void setUpPlayer() {
        // Player
        player = new Player(100, new GameStats(20, 10, 80, 20));
        player.setPosition(new Vector2D(1100, 400));
        player.setDimensions(new Vector2D(100, 150));
        player.setSpriteName("player");
        player.addTag("Not Attackable");
        player.getGameEventListenerManager().registerEventListener(new GameEventListener<DeathEvent>() {
            @Override
            protected void run(DeathEvent deathEvent, Engine engine) {
                engine.reset(setUpDefeatScene());
                engine.enqueueDialogue(Dialogue.fromString("You Lose"));
            }
        });
        
        // dialogue system demonstration
        GameAction inventoryAction = new GameAction("Inventory", Dialogue.empty(), (engine) -> {

            Dialogue currentDialogue = Dialogue.fromString("You have: ");
            int count = 1;
            for (Item item : player.getItems()) {
                currentDialogue.addLine(
                        new DialogueLine(item.name(), Color.black, Dialogue.getDefaultFont().deriveFont(Font.BOLD)));
                count++;
                if (count >= 8) {
                    engine.enqueueDialogue(currentDialogue);
                    currentDialogue = new Dialogue();
                    count = 0;
                }
            }
            engine.enqueueDialogue(currentDialogue);
        });
        player.addGameAction(inventoryAction);
    }

    private static void setUpGlobalGameObjects(){
        globalGameObjects.add(player);
        //settings button
        GameObject settingsButton = new GameObject(new Vector2D(25, 25), new Vector2D(50, 50), "settings_icon");
        globalGameObjects.add(settingsButton);

        GameAction changeDirectionalControls = new GameAction(engine.getCurrentDirectionalKeyMaps().name(), Dialogue.fromString("Change directional controls"), (engine, action)->{
            String currentDirectionalKeyMapsName = engine.getCurrentDirectionalKeyMaps().name();
            
            if(currentDirectionalKeyMapsName == DirectionalKeyMaps.ARROW_KEYS.name()){
                engine.setKeyMaps(DirectionalKeyMaps.WASD);
            }
            else if(currentDirectionalKeyMapsName == DirectionalKeyMaps.WASD.name()){
                engine.setKeyMaps(DirectionalKeyMaps.HJKL);
            }
            else if(currentDirectionalKeyMapsName == DirectionalKeyMaps.HJKL.name()){
                engine.setKeyMaps(DirectionalKeyMaps.ARROW_KEYS);
            }

            action.setName(engine.getCurrentDirectionalKeyMaps().name());
            
        });

        changeDirectionalControls.setClosesMenu(false);

        GameAction changeAcceptControls = new GameAction(engine.getCurrentAcceptKeyMaps().name(), Dialogue.fromString("Change confirm/cancel controls"), (engine, action)->{
            String currentAcceptKeyMapsName = engine.getCurrentAcceptKeyMaps().name();
            
            if(currentAcceptKeyMapsName == AcceptKeyMaps.ZX.name()){
                engine.setKeyMaps(AcceptKeyMaps.ENTER_ESC);
            }
            else if(currentAcceptKeyMapsName == AcceptKeyMaps.ENTER_ESC.name()){
                engine.setKeyMaps(AcceptKeyMaps.ZX);
            }

            action.setName(engine.getCurrentAcceptKeyMaps().name());
            
        });
        changeAcceptControls.setClosesMenu(false);

        GameAction restart = new GameAction("Restart", Dialogue.fromString("Restart"), (engine) -> {restart();});

        settingsButton.addGameAction(changeDirectionalControls);
        settingsButton.addGameAction(changeAcceptControls);
        settingsButton.addGameAction(restart);
    }
}
