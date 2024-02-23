package ru.rmntim.cli.commands;

import java.util.List;

public abstract class Command {
    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * @param arguments list of arguments to the command, not including the command name.
     * @throws BadCommandArgumentsException if argument list is not valid
     */
    public abstract void execute(List<String> arguments);

    @Override
    public String toString() {
        return name + " - " + description;
    }
}
