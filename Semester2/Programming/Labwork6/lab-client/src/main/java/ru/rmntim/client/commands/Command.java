package ru.rmntim.client.commands;

import ru.rmntim.client.logic.ExecutionContext;
import ru.rmntim.common.network.responses.Response;

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
     * Sends a request to the server.
     *
     * @param ctx  execution context
     * @param args command arguments
     */
    public abstract Response sendRequest(ExecutionContext ctx, List<String> args);
}
