package ru.rmntim.cli.logic;

import ru.rmntim.cli.commands.Command;
import ru.rmntim.cli.commands.HelpCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistryBuilder {
    private final Map<String, Command> commandRegistry = new HashMap<>();

    public CommandRegistryBuilder register(final Command command) {
        commandRegistry.put(command.getName(), command);
        return this;
    }

    public Map<String, Command> create() {
        register(new HelpCommand(commandRegistry.values()));
        return commandRegistry;
    }
}
