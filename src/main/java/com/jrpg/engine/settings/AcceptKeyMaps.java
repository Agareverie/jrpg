package com.jrpg.engine.settings;

import java.awt.event.KeyEvent;

public interface AcceptKeyMaps {
    int confirm();
    int cancel();
    String name();

    AcceptKeyMaps ZX = new AcceptKeyMaps() {
        @Override
        public int confirm() {
            return KeyEvent.VK_Z;
        }

        @Override
        public int cancel() {
            return KeyEvent.VK_X;
        }

        @Override
        public String name(){
            return "Z/X";
        }
    };

    AcceptKeyMaps ENTER_ESC = new AcceptKeyMaps() {
        @Override
        public int confirm() {
            return KeyEvent.VK_ENTER;
        }

        @Override
        public int cancel() {
            return KeyEvent.VK_ESCAPE;
        }

        
        @Override
        public String name(){
            return "Enter/Escape";
        }
    };
}
