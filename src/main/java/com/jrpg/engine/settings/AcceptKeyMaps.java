package com.jrpg.engine.settings;

import java.awt.event.KeyEvent;

public interface AcceptKeyMaps {
    int confirm();
    int cancel();

    AcceptKeyMaps ZX = new AcceptKeyMaps() {
        @Override
        public int confirm() {
            return KeyEvent.VK_Z;
        }

        @Override
        public int cancel() {
            return KeyEvent.VK_X;
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
    };
}
