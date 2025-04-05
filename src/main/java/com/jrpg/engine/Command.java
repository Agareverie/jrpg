package com.jrpg.engine;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A Command used in the game engine. The commands may be initialized by the class itself or via the command builder.
 *
 * @param name The command name used in the console
 * @param parameters The parameters also used in the console
 * @param helpText The text displayed when the <code>help</code> command is entered
 * @param run A function that takes in an <code>Engine</code> argument and returns void, will be run when the command runs
 */
public record Command(String name, String[] parameters, String helpText, Consumer<Engine> run) {
    /** The regex for <code>kebab-case</code> names */
    public static final String REGEX = "^[a-z]+(-[a-z]+)*$";

    /**
     * A defensive constructor to validate the command.
     * @param name Cannot be null or empty, it should ideally be in <code>lowercase</code> or <code>kebab-case</code>
     * @param parameters Cannot be null, the inner strings also cannot be null or empty, and should ideally be in <code>lowercase</code> or <code>kebab-case</code>
     * @param helpText Cannot be null or empty.
     * @param run Cannot be null.
     */
    public Command {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(parameters, "parameters cannot be null");
        Objects.requireNonNull(helpText, "help text cannot be null");
        Objects.requireNonNull(run, "run command cannot be null");

        if (name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty");
        }

        if (helpText.isEmpty()) {
            throw new IllegalArgumentException("help text cannot be empty");
        }

        if (!name.matches("^[a-z]+(-[a-z]+)*$")) {
            throw new IllegalArgumentException("name can only contain lowercase letters");
        }

        for (String parameter : parameters) {
            if (!parameter.matches("^[a-z]+(-[a-z]+)*$")) {
                throw new IllegalArgumentException("parameter can only contain lowercase letters");
            }
        }
    }

    public static class Builder {
        private String name;
        private String[] parameters = new String[0];
        private String helpText;
        private Consumer<Engine> run = (engine) -> {};

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder parameters(String... parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder helpText(String helpText) {
            this.helpText = helpText;
            return this;
        }

        public Builder run(Consumer<Engine> run) {
            this.run = run;
            return this;
        }

        public Command build() {
            return new Command(name, parameters, helpText, run);
        }
    }
}
