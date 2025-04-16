package com.jrpg.rendering.graphics;

import java.awt.*;

import com.jrpg.rendering.Coordinate;

public record Sprite(Coordinate position, Coordinate dimensions, Image image) implements Drawable {

    public static Sprite centered(Coordinate position, Coordinate dimensions, Image image) {
        return new Sprite(Coordinate.toCentered(position, dimensions), dimensions, image);
    }

    public static Sprite centered(Coordinate position, Image image) {
        return new Sprite(Coordinate.toCentered(position, new Coordinate(image.getWidth(null), image.getHeight(null))), image);
    }

    public Sprite(Coordinate position, Image image) {
        this(position, new Coordinate(image.getWidth(null), image.getHeight(null)), image);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, position.getX(), position.getY(), dimensions.getX(), dimensions.getY(), null);
    }   
}
