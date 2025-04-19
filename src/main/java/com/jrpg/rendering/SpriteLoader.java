package com.jrpg.rendering;

import java.awt.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

public class SpriteLoader {
    private static String defaultSpriteName = "default";
    private final HashMap<String, Image> sprites;

    private static final class SingletonHolder {
        public static final SpriteLoader INSTANCE = new SpriteLoader();
    }

    public static SpriteLoader getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private SpriteLoader() {
        sprites = new HashMap<>();

        Stream<Path> paths = loadSpritesFolder();

        paths.filter(Files::isRegularFile).forEach(path -> {
            String fileName = path.getFileName().toString();

            String[] spriteNameParts = fileName.split("\\.");
            StringJoiner spriteNameJoiner = new StringJoiner(".");

            for (int i = 0; i < spriteNameParts.length - 1; i++) {
                spriteNameJoiner.add(spriteNameParts[i]);
            }

            String spriteName = spriteNameJoiner.toString();

            Image sprite;
            try {
                sprite = ImageIO.read(path.toFile());
                sprites.put(spriteName, sprite);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        paths.close();

        if(!sprites.containsKey(defaultSpriteName)){
            throw new RuntimeException("no default sprite with name " + defaultSpriteName + " found");
        }
    }

    public static Image getSprite(String name) {
        HashMap<String, Image> sprites = getInstance().sprites;

        if (sprites.containsKey(name)) {
            return sprites.get(name);
        }

        return sprites.get(defaultSpriteName);
    }

    private static Stream<Path> loadSpritesFolder(){
        URL spritesUrl = SpriteLoader.class.getResource("/sprites");

        try {
            Objects.requireNonNull(spritesUrl, "Sprites folder not found");
            return Files.walk(Path.of(spritesUrl.toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setDefaultSpriteName(String defaultSpriteName) {
        if (!getInstance().sprites.containsKey(defaultSpriteName)) {
            throw new RuntimeException("no default sprite with name " + defaultSpriteName + " found");
        }

        SpriteLoader.defaultSpriteName = defaultSpriteName;
    }
    
}
