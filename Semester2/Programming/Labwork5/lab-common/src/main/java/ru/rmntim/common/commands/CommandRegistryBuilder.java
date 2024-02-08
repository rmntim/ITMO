package ru.rmntim.common.commands;

import java.util.HashMap;

public class CommandRegistryBuilder {
    private final HashMap<String, Command> commands = new HashMap<>();

    public CommandRegistryBuilder() {
    }

    public CommandRegistryBuilder register(String commandName, Command command) {
        commands.put(commandName, command);
        return this;
    }

    public HashMap<String, Command> build() {
        register("help", new HelpCommand(commands.values().stream().toList()));
        return commands;
    }
}
