package com.jrpg;

import java.awt.*;
import javax.swing.*;

import com.jrpg.dimensions.Dimensions;
import com.jrpg.rendering.*;
import com.jrpg.rendering.graphics.*;
import com.jrpg.rendering.graphics.Rectangle;

public class Main {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsRenderer renderer = new GraphicsRenderer(frame, Dimensions.DEFAULT);

        Coordinate position = new Coordinate(0, 0);
        Coordinate velocity = new Coordinate(16, 9);

        // More dots work now
        Image sprite = SpriteLoader.getSprite("sensei.sprite");

        while (true) {
            renderer.clear();

            position = position.add(velocity);

            // Dimensions now stored in a separate class
            if (position.getX() + 100 > Dimensions.WIDTH || position.getX() < 0) {
                velocity.setX(x -> -x);  // Fancy lambda setters
            }


            if (position.getY() + 100 > Dimensions.HEIGHT || position.getY() < 0) {
                velocity.setY(y -> -y);
            }

            renderer.add(new Rectangle(position, new Coordinate(100, 100), Color.GRAY));
            renderer.add(new Sprite(position, new Coordinate(100, 100), sprite));
            // Fixed issue where text color changes because of rectangle
            renderer.add(new Text("Test", position.add(0,-20), new Font("Arial", Font.PLAIN, 20), Color.BLACK));
            renderer.add(new Text("Test", position.add(0,-60), new Font("Serif", Font.BOLD, 40), Color.BLACK));

            renderer.render();

            Thread.sleep(10);
        }
    }
}