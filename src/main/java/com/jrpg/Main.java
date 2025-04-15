package com.jrpg;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.jrpg.engine.*;
import com.jrpg.renderer.Renderer;
import com.jrpg.renderer.SpriteLoader;
import com.jrpg.renderer.graphics.Rectangle;
import com.jrpg.renderer.graphics.Sprite;

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
        Renderer renderer = new Renderer(frame);
        Coordinate position = new Coordinate(100, 300);
        Coordinate velocitiy = new Coordinate(16, -9);

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

            renderer.render();

            //poor man's time.sleep()
            for(int j = 0; j < 1000; j++) {System.out.println(j);};
        }
    }
}