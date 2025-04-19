package com.jrpg.example_game.events;

import com.jrpg.example_game.GameEvent;
public class DeathEvent implements GameEvent {
    
    @Override
    public String getEventName() {
        return "Death";
    }
}
