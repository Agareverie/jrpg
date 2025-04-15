package com.jrpg;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import com.jrpg.engine.*;
import com.jrpg.rendering.*;
import com.jrpg.rendering.graphics.*;

public class Main {
    public static void main(String[] args) {
        // Command command = new Command.Builder()
        //         .name("exit")
        //         .run((engine) -> {
        //             System.out.println("Exit application");
        //             System.exit(0);
        //         })
        //         .helpText("Exit the program")
        //         .build();

        // command.run().accept(null);
    
        //copied this from the other demo
        JFrame frame = new JFrame("JRPG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ArrayList<Scene> scenes = new ArrayList<Scene>();

        Scene scene = new Scene();
        scene.add(new GameObject(new Vector2D(0, 0), new Vector2D(70, 70), ""));
        scene.add(new GameObject(new Vector2D(100, 100), new Vector2D(70, 70), "testSprite1"));
        scene.add(new GameObject(new Vector2D(200, 100), new Vector2D(70, 70), "testSprite2"));
        scenes.add(scene);

        Engine engine = new Engine(frame, scenes);
        
        while (true){
            engine.update();
            try{
                Thread.sleep(Math.round(1000/24));
            } catch(InterruptedException e){

            }
        }
    }
}