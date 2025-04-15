package com.jrpg.rendering;

import java.awt.Image;
import java.io.IOException;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import javax.imageio.ImageIO;

public class SpriteLoader {
    private final Map<String, Image> sprites;
    private static String defaultSpriteName = "default";

    private static final class SingletonHolder {
        public static final SpriteLoader INSTANCE = new SpriteLoader();
    }

    public static SpriteLoader getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static Optional<Path> loadSpritesFolder() {
        URL spritesUrl = SpriteLoader.class.getResource("/sprites");

        if (spritesUrl == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(Path.of(spritesUrl.toURI()));

        } catch (URISyntaxException e) {
            return Optional.empty();
        }
    }

    private SpriteLoader() {
        sprites = new HashMap<>();

        try (var folder = Files.walk(loadSpritesFolder().orElseThrow(NullPointerException::new))) {
            folder.filter(Files::isRegularFile).forEach(path -> {
                String fileName = path.getFileName().toString();

                String[] spriteNameParts = fileName.split("\\.");
                StringJoiner spriteNameJoiner = new StringJoiner(".");

                for (int i = 0; i < spriteNameParts.length - 1; i++) {
                    spriteNameJoiner.add(spriteNameParts[i]);
                }

                String spriteName = spriteNameJoiner.toString();

                try {
                    Image sprite = ImageIO.read(path.toFile());
                    sprites.put(spriteName, sprite);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!sprites.containsKey(defaultSpriteName)) {
            throw new RuntimeException("no default sprite with name " + defaultSpriteName + " found");
        }
    }

    public static Image getSprite(String name) {
        var sprites = getInstance().sprites;

        if (sprites.containsKey(name)) {
            return sprites.get(name);
        }

        return sprites.get(defaultSpriteName);
    }

    public static void setDefaultSpriteName(String defaultSpriteName) {
        if (!getInstance().sprites.containsKey(defaultSpriteName)) {
            throw new RuntimeException("no default sprite with name " + defaultSpriteName + " found");
        }
        SpriteLoader.defaultSpriteName = defaultSpriteName;
    }
}
