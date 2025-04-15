package com.jrpg.renderer;

import java.awt.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

//TODO: switch this to a singleton
public class SpriteLoader {
    
    private static boolean initialized = false;
    private static HashMap<String, Image> sprites;

    //maybe make this mofifiable at runtime?
    //for now it's constant from startup
    private static String defaultSpriteName = "default";

    private static void initialize(){
        sprites = new HashMap<String, Image>();

        URL url = SpriteLoader.class.getResource("/sprites");
        
        Stream<Path> paths;
        try {
            paths = Files.walk(Path.of(url.toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        paths.filter(Files::isRegularFile).forEach(path -> {
            String fileName = path.getFileName().toString();

            //don't name our files with more than 1 .
            //(or modify this piece of code to support that first)
            String spriteName = fileName.split("\\.")[0];

            Image sprite;
            try {
                sprite = ImageIO.read(path.toFile());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            sprites.put(spriteName, sprite);
        });

        paths.close();
        
        if(!sprites.containsKey(defaultSpriteName)){
            throw new RuntimeException("no default sprite with name " + defaultSpriteName + " found");
        }

    }

    public static Image getSprite(String name){
        if(!SpriteLoader.initialized) SpriteLoader.initialize();

        if(sprites.containsKey(name)) return sprites.get(name);
        else return sprites.get(defaultSpriteName);
    }
    
}
