package com.jrpg;

import javax.swing.JFrame;
import java.awt.Font;
import java.awt.Color;

import com.jrpg.engine.*;
import com.jrpg.engine.components.*;
import com.jrpg.engine.settings.AcceptKeyMaps;
import com.jrpg.engine.settings.DirectionalKeyMaps;
import com.jrpg.example_game.*;
import com.jrpg.example_game.events.*;

//for this example I put all the set-up into here
//,but you can easily see how you could set this up to be more general and be in other files
//(like the buy actions could be programmatically generated, player should probably be its own class etc.)
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JRPG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        ExampleScene weaponsShop = new ExampleScene("Shop");
        ExampleScene forest = new ExampleScene("Forest", "testBackground");
        ExampleScene animationDemonstration = new ExampleScene("Animation Demonstration");

        //Player
        GameCharacter player = new GameCharacter("Player", 100, new GameStats(20, 10, 80, 20));
        player.setDescription(Dialogue.fromString("You"));
        player.setPosition(new Vector2D(1100, 400));
        player.setDimensions(new Vector2D(100, 150));
        player.setSpriteName("testSprite2");
        player.addTag("Not Attackable");
        player.getGameEventListenerManager().registerEventListener(new GameEventListener<DeathEvent>() {
            @Override
            protected void run(DeathEvent deathEvent, Engine engine) {
                engine.enqueueDialogue(Dialogue.fromString("You Lose"));
                //in the actual game, the engine should instead switch to a loss scene
                engine.getCurrentScene().getGameObjects().forEach((gameObject) -> {
                    gameObject.setSelectable(false);
                    gameObject.setSpriteName(null);
                });
            }
        });

        //dialogue system demonstration
        GameAction inventoryAction = new GameAction("Inventory", Dialogue.empty(), (engine) -> {

            Dialogue currentDialogue = Dialogue.fromString("You have: ");
            int count = 1;
            for (Item item : player.getItems()) {
                currentDialogue.addLine(new DialogueLine(item.name(), Color.black, Dialogue.getDefaultFont().deriveFont(Font.BOLD)));
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

        //shopkeeper
        GameCharacter shopkeeper = new GameCharacter("Shopkeeper", 50, new GameStats(20, 10, 80, 20));
        shopkeeper.setDescription(Dialogue.fromString("The shopkeeper"));
        shopkeeper.setSpriteName("human");
        shopkeeper.setPosition(new Vector2D(700, 200));
        shopkeeper.setDimensions(new Vector2D(120, 180));

        shopkeeper.getGameEventListenerManager().registerEventListener(new GameEventListener<SawAttackEvent>() {
            @Override
            protected void run(SawAttackEvent sawAttackEvent, Engine engine) {
                engine.enqueueDialogue(Dialogue.fromString("Shopkeeper:\nWhy are you attacking my " + (sawAttackEvent.getDefender().getName() + "?")));
            }
        });

        shopkeeper.getGameEventListenerManager().registerEventListener(new GameEventListener<AttackedEvent>() {
            @Override
            protected void run(AttackedEvent attackedEvent, Engine engine) {
                engine.enqueueDialogue(Dialogue.fromString("""
                        Shopkeeper:
                        Ouch!
                        """)
                );
            }
        });

        //buy actions
        GameAction buySword = new GameAction("Buy Sword", Dialogue.fromString("Buy a sword, increases Attack."), (engine) -> {
            player.addItem(new Item("Sword", new GameStats(30, 0, 0, 0)));
            engine.enqueueDialogue(Dialogue.fromString("You bought a sword!"));
        });

        GameAction buyShield = new GameAction("Buy Shield", Dialogue.fromString("Buy a shield, increases Defense."), (engine) -> {
            player.addItem(new Item("Shield", new GameStats(0, 30, 0, 0)));
            engine.enqueueDialogue(Dialogue.fromString("You bought a shield!"));
        });

        GameAction buyGlasses = new GameAction("Buy Glasses", Dialogue.fromString("Buy a pair of glasses, makes you see better."), (engine) -> {
            player.addItem(new Item("Glasses", new GameStats(0, 0, 30, 0)));
            engine.enqueueDialogue(Dialogue.fromString("You bought a pair of glasses!"));
        });

        GameAction buyBoots = new GameAction("Buy Boots", Dialogue.fromString("Buy a pair of boots, made specifically for dodging."), (engine) -> {
            player.addItem(new Item("Boots", new GameStats(0, 0, 0, 30)));
            engine.enqueueDialogue(Dialogue.fromString("You bought some boots!"));
        });

        GameAction talk = new GameAction("Talk", Dialogue.empty(), (engine) -> {
            engine.enqueueDialogue(Dialogue.fromString("""
                    Shopkeeper:
                    Welcome, adventurer, to my humble weapons shop.
                    Please buy anything that catches your eye.
                    """)
            );
        });

        shopkeeper.addGameAction(buySword);
        shopkeeper.addGameAction(buyShield);
        shopkeeper.addGameAction(buyGlasses);
        shopkeeper.addGameAction(buyBoots);
        shopkeeper.addGameAction(talk);

        //furniture (for parity)
        ExampleGameObject shelves = new ExampleGameObject("Shelves", 50, new GameStats(0, 60, 0, -40));
        shelves.setDescription(Dialogue.fromString("Shelves"));
        shelves.setSpriteName("shelves");
        shelves.setPosition(new Vector2D(200, 100));
        shelves.setDimensions(new Vector2D(200, 100));

        ExampleGameObject weaponRack = new ExampleGameObject("Weapons Rack", 50, new GameStats(0, 60, 0, -40));
        weaponRack.setDescription(Dialogue.fromString("Weapons Rack"));
        weaponRack.setSpriteName("weaponRack");
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
        door.addDestination(forest);
        door.addDestination(animationDemonstration);

        weaponsShop.add(player);
        weaponsShop.add(shopkeeper);
        weaponsShop.add(shelves);
        weaponsShop.add(weaponRack);
        weaponsShop.add(box);
        weaponsShop.add(door);

        forest.add(player);
        forest.add(new Goblin(new Vector2D(200, 250)));
        forest.add(new Goblin(new Vector2D(400, 250)));
        forest.add(new Goblin(new Vector2D(600, 250)));
        forest.add(door);

        //animation demonstration
        //I left it pretty open so you should be able to control it however you want
        //,but it's basically just a timer that is allowed to control anything (which it's supposed to control animations)
        //you could have it make a game object rapidly change sprites to create animations and stuff like that
        GameObject animationTarget = new GameObject(new Vector2D(300, 300), new Vector2D(400, 400), "testSprite1");
        ShrinkAndGrow ShrinkAndGrow = new ShrinkAndGrow(animationTarget, .25);

        animationTarget.addGameAction(new GameAction("Start Animation", Dialogue.empty(), (engine) -> {
            engine.getCamera().addGameAnimation(ShrinkAndGrow);
            ShrinkAndGrow.setActive(true);
        }));


        animationTarget.addGameAction(new GameAction("Stop Animation", Dialogue.empty(), (engine) -> {
            ShrinkAndGrow.setActive(false);
        }));

        animationDemonstration.add(player);
        animationDemonstration.add(animationTarget);
        animationDemonstration.add(door);

        // example conditional action
        GameAction attack = new GameAction("Attack", Dialogue.fromString("Attack!!!"),
                (engine) -> {
                    CombatManager.initiateAttack(player, (ExampleGameObject) engine.getCurrentSelectedGameObject(), engine);
                },
                (gameObject) -> {
                    if (gameObject instanceof ExampleGameObject exampleGameObject) {
                        return !exampleGameObject.hasTag("Not Attackable");
                    }
                    return false;
                }
        );

        Engine engine = new Engine(frame, weaponsShop);
        engine.setKeyMaps(DirectionalKeyMaps.WASD, AcceptKeyMaps.ENTER_ESC);

        engine.addGeneralGameAction(attack);

        engine.loop();
    }
}