package com.jrpg;

import javax.swing.JFrame;

import com.jrpg.example_game.*;

//for this example I put all the set-up into here
//,but you can easily see how you could set this up to be more general and be in other files
//(like the buy actions could be programmatically generated, player should probably be its own class etc.)
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JRPG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ExampleGame.initialize(frame);
    }
}