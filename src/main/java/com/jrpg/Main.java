package com.jrpg;

import java.awt.*;
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
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsRenderer renderer = new GraphicsRenderer(frame);
        Coordinate position = new Coordinate(0, 0);
        Coordinate velocitiy = new Coordinate(16, 9);

        Image sprite = SpriteLoader.getSprite("testSprite1");
        //Image sprite = SpriteLoader.getSprite("testSprite2");
        //Image sprite = SpriteLoader.getSprite("invalidSprite");

        while (true){
            renderer.clear();

            position = position.add(velocitiy);

            //im probably switching Coordinate back to using public for x and y
            //but i was just experimenting
            if(position.getX() + 100 > 1200 || position.getX() < 0){
                velocitiy.setX(velocitiy.getX() * -1);
            }

            
            if(position.getY() + 100 > 675 || position.getY() < 0){
                velocitiy.setY(velocitiy.getY() * -1);
            }

            renderer.add(new Sprite(position, new Coordinate(100, 100), sprite));
            renderer.add(new Text("Test", position.add(0,-20), new Font("arial", Font.PLAIN, 20)));
            renderer.add(new Text("Test", position.add(0,-60), new Font("Serif", Font.BOLD, 40)));
            renderer.render();

            //poor man's time.sleep()
            for(int j = 0; j < 200000000; j++) {};
        }
    }
}