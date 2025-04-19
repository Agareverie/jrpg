package com.jrpg;

import java.awt.*;

import javax.swing.*;

import com.jrpg.engine.*;
import com.jrpg.engine.components.*;
import com.jrpg.example_game.*;
import com.jrpg.example_game.events.*;

//for this example i put all the set up into here
//but you can easily see how you could set this up to be more general and be in other files
//(like the buy actions could be programmatically generated, player should probably be it's own class etc.)
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
        player.getGameEventListenerManager().registerEventListener(new GameEventListener<DeathEvent>((gameEvent, engine)->{
            engine.enqueueDialogue(Dialogue.fromString("You Lose"));
            //in the actual game, the engine should instead switch to a lose scene
            engine.getCurrentScene().getGameObjects().forEach((gameObject) -> {
                gameObject.setSelectable(false);
                gameObject.setSpriteName(null);
            });
        }));

        //dialogue system demonstration
        GameAction inventoryAction = new GameAction("Inventory", (engine) -> {

            Dialogue currentDialogue = Dialogue.fromString("You have: ");
            int count = 1;
            for (Item item : player.getItems()) {
                currentDialogue.addLine(new DialogueLine(item.getName(), Color.black, Dialogue.getDefaultFont().deriveFont(Font.BOLD)));
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

        shopkeeper.getGameEventListenerManager().registerEventListener(new GameEventListener<SawAttackEvent>((gameEvent, engine) -> {
            engine.enqueueDialogue(Dialogue.fromString("Shopkeeper:\nWhy are you attacking my " + (gameEvent.getDefender().getName())));
        }));

        shopkeeper.getGameEventListenerManager().registerEventListener(new GameEventListener<AttackedEvent>((gameEvent, engine) -> {
            engine.enqueueDialogue(Dialogue.fromString("Shopkeeper:\nOuch!"));
        }));

        //buy actions
        GameAction buySword = new GameAction("Buy Sword", (engine) -> {
            player.addItem(new Item("Sword", new GameStats(30, 0, 0, 0)));
            engine.enqueueDialogue(Dialogue.fromString("you bought a sword"));
        });

        GameAction buyShield = new GameAction("Buy Shield", (engine) -> {
            player.addItem(new Item("Shield", new GameStats(0, 30, 0, 0)));
            engine.enqueueDialogue(Dialogue.fromString("you bought a shield"));
        });

        GameAction buyGlasses = new GameAction("Buy Glasses", (engine) -> {
            player.addItem(new Item("Glasses", new GameStats(0, 0, 30, 0)));
            engine.enqueueDialogue(Dialogue.fromString("you bought a pair of glasses"));
        });

        GameAction buyBoots = new GameAction("Buy Boots", (engine) -> {
            player.addItem(new Item("Boots", new GameStats(0, 0, 0, 30)));
            engine.enqueueDialogue(Dialogue.fromString("you bought some boots"));
        });

        GameAction talk = new GameAction("talk", (engine) -> {
            engine.enqueueDialogue(Dialogue.fromString("Shopkeeper:\nwelcome adventurer to my humble weapons shop\nplease buy anything that catches your eyes"));
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
        //i left it pretty open so you should be able to control it however you want
        //but it's basically just a timer that is allowed to control anything (which it's supposed to control animations)
        //you could have it make a game object rapidly change sprites to create animations and stuff like that
        GameObject animationTarget = new GameObject(new Vector2D(300, 300), new Vector2D(400,400), "testSprite1");
        ShrinkAndGrow ShrinkAndGrow = new ShrinkAndGrow(animationTarget, .25);

        animationTarget.addGameAction(new GameAction("Start Animation", (engine) -> {
            engine.getCamera().addGameAnimation(ShrinkAndGrow);
            ShrinkAndGrow.setActive(true);
        }));

        
        animationTarget.addGameAction(new GameAction("Stop Animation", (engine) -> {
            ShrinkAndGrow.setActive(false);
        }));

        animationDemonstration.add(player);
        animationDemonstration.add(animationTarget);
        animationDemonstration.add(door);

        // example conditional action
        GameAction attack = new GameAction("Attack", (engine) -> {
            CombatManager.initiateAttack((ExampleGameObject) player, (ExampleGameObject) engine.getCurrentSelectedGameObject(), engine);
            }, (gameObject) -> {
            if(!(gameObject instanceof ExampleGameObject exampleGameObject)) {
                return false;
            }
            return !exampleGameObject.hasTag("Not Attackable");
        });


        Engine engine = new Engine(frame, weaponsShop);

        engine.addGeneralGameAction(attack);

        engine.loop();
    }
}