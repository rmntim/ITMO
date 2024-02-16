package ru.rmntim.server.logic;

import ru.rmntim.common.commands.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistryBuilder {
    private final Map<String, Command> commandRegistry = new HashMap<>();

    public CommandRegistryBuilder register(final Command command) {
        commandRegistry.put(command.getName(), command);
        return this;
    }

    public Map<String, Command> create() {
        return commandRegistry;
    }
}
