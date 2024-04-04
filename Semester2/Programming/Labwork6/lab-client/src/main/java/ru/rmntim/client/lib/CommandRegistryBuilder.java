package ru.rmntim.client.lib;

import ru.rmntim.common.commands.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CommandRegistryBuilder {
    private final Map<String, Supplier<Command>> suppliers = new HashMap<>();
    private final Map<String, String> descriptions = new HashMap<>();

    /**
     * Class used for managing available commands.
     */
    public CommandRegistryBuilder() {
    }

    /**
     * Adds a command to the registry.
     *
     * @param name            name of the command
     * @param description     description of the command
     * @param commandSupplier function that returns the command, or {@code null} if the command is not supposed to be sent to the server
     * @return this, because this is a builder
     */
    public CommandRegistryBuilder register(String name, String description, Supplier<Command> commandSupplier) {
        suppliers.put(name, commandSupplier);
        descriptions.put(name, description);
        return this;
    }

    /**
     * Builds the command registry and adds the `help` and `exit` commands.
     *
     * @return the command registry
     */
    public Map<String, Supplier<Command>> build() {
        register("help", "Show list of commands", () -> {
            descriptions.entrySet().forEach(System.out::println);
            return null;
        });

        register("exit", "Exit the program", () -> {
            System.exit(0);
            return null;
        });

        return suppliers;
    }
}
