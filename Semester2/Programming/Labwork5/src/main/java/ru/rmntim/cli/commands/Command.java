package ru.rmntim.cli.commands;

import ru.rmntim.cli.logic.ExecutionContext;

import java.util.List;
import java.util.StringJoiner;

public abstract class Command {
    private final String name;
    private final String description;
    private final List<String> parameters;

    /**
     * @param name        command name
     * @param description command description
     * @throws NullPointerException     if name or description or is null
     * @throws IllegalArgumentException if name or description is empty
     */
    public Command(String name, String description) {
        this(name, description, List.of());
    }

    /**
     * @param name        command name
     * @param description command description shown in help
     * @param parameters  inline command parameters
     * @throws NullPointerException     if name, description or parameters are null
     * @throws IllegalArgumentException if name or description is empty
     */
    public Command(String name, String description, List<String> parameters) {
        if (name == null || description == null || parameters == null) {
            throw new NullPointerException();
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name can't be empty");
        }
        if (description.isBlank()) {
            throw new IllegalArgumentException("Description can't be empty");
        }
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    /**
     * @param arguments list of arguments to the command, not including the command name
     * @param context   execution context
     * @throws ru.rmntim.cli.exceptions.BadCommandArgumentsException if argument list is not valid
     */
    public abstract void execute(List<String> arguments, ExecutionContext context);

    @Override
    public String toString() {
        var sj = new StringJoiner(", ");
        parameters.forEach(sj::add);
        var result = name;
        if (sj.length() > 0) {
            result += " [" + sj + "]";
        }
        result += " - " + description;
        return result;
    }
}
