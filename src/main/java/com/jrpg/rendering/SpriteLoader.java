package com.jrpg.rendering;

import java.awt.*;
import java.io.IOException;
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
                sprite = ImageIO.read(Files.newInputStream(path));
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

    @SuppressWarnings("resource")
    private static Stream<Path> loadSpritesFolder() {
        try {
            URL spritesUrl = SpriteLoader.class.getResource("/sprites");
            Objects.requireNonNull(spritesUrl, "Sprites folder not found");
            URI uri = spritesUrl.toURI();

            if ("jar".equals(uri.getScheme())) {
                FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap());
                Path pathInJar = fs.getPath("/sprites");
                return Files.walk(pathInJar)
                            .onClose(() -> {
                                try {
                                    fs.close();
                                } catch (IOException ignored) {}
                            });
            } else {
                // Regular file path (e.g., in IDE)
                return Files.walk(Path.of(uri));
            }
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
