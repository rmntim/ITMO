package ru.rmntim.server.logic;

import ru.rmntim.server.commands.Command;
import ru.rmntim.server.commands.HelpCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for creating registry of commands (basically a {@code Map<String, Command>}), which is used in {@link ExecutionContext}.
 */
public class CommandRegistryBuilder {
    private final Map<String, Command> commandRegistry = new HashMap<>();

    /**
     * Adds command to registry by name
     *
     * @param command command to add to the registry
     * @return this builder
     */
    public CommandRegistryBuilder register(final Command command) {
        commandRegistry.put(command.getName(), command);
        return this;
    }

    /**
     * @return command registry, with all registered commands and help command
     */
    public Map<String, Command> build() {
        register(new HelpCommand(commandRegistry.values()));
        return commandRegistry;
    }
}
