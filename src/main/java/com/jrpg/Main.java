package com.jrpg;

import com.jrpg.engine.Command;

public class Main {
    public static void main(String[] args) {
        Command command = new Command.Builder()
                .name("exit")
                .run((engine) -> {
                    System.out.println("Exit application");
                    System.exit(0);
                })
                .helpText("Exit the program")
                .build();

        command.run().accept(null);
    }
}