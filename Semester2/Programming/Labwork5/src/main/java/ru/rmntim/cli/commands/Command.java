package ru.rmntim.cli.commands;

import java.io.BufferedReader;
import java.util.List;
import java.util.StringJoiner;

public abstract class Command {
    private final String name;
    private final String description;
    private final List<String> parameters;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
        this.parameters = List.of();
    }

    public Command(String name, String description, List<String> parameters) {
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getParameters() {
        return parameters;
    }

    /**
     * @param arguments list of arguments to the command, not including the command name.
     * @param reader
     * @throws ru.rmntim.cli.exceptions.BadCommandArgumentsException if argument list is not valid
     */
    public abstract void execute(List<String> arguments, BufferedReader reader);

    @Override
    public String toString() {
        var sj = new StringJoiner(", ");
        parameters.forEach(sj::add);
        var result = name;
        if (sj.length() > 0) {
            result += " [" + sj.toString() + "]";
        }
        result += " - " + description;
        return result;
    }
}
