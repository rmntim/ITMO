package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;

/**
 * Base class for all commands.
 */
public abstract class Command {
    private final String name;
    private final String description;

    /**
     * @param name        command name
     * @param description command description
     */
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
     * Accepts visitor and returns response.
     *
     * @param visitor command visitor
     * @return response
     */
    public abstract Response accept(Visitor visitor);

    public interface Visitor {
        Response visit(Info info);
    }
}
