package com.jrpg.engine.settings;

import java.awt.event.KeyEvent;

public interface DirectionalKeyMaps {
    int up();
    int down();
    int left();
    int right();
    String name();

    DirectionalKeyMaps WASD = new DirectionalKeyMaps() {
        @Override
        public int up() {
            return KeyEvent.VK_W;
        }

        @Override
        public int down() {
            return KeyEvent.VK_S;
        }

        @Override
        public int left() {
            return KeyEvent.VK_A;
        }

        @Override
        public int right() {
            return KeyEvent.VK_D;
        }

        @Override
        public String name(){
            return "WASD";
        }
    };

    DirectionalKeyMaps ARROW_KEYS = new DirectionalKeyMaps() {
        @Override
        public int up() {
            return KeyEvent.VK_UP;
        }

        @Override
        public int down() {
            return KeyEvent.VK_DOWN;
        }

        @Override
        public int left() {
            return KeyEvent.VK_LEFT;
        }

        @Override
        public int right() {
            return KeyEvent.VK_RIGHT;
        }

        @Override
        public String name(){
            return "Arrow Keys";
        }
    };

    DirectionalKeyMaps HJKL = new DirectionalKeyMaps() {
        @Override
        public int up() {
            return KeyEvent.VK_H;
        }

        @Override
        public int down() {
            return KeyEvent.VK_J;
        }

        @Override
        public int left() {
            return KeyEvent.VK_K;
        }

        @Override
        public int right() {
            return KeyEvent.VK_L;
        }

        @Override
        public String name(){
            return "HJKL";
        }
    };
}
