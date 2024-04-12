package ru.rmntim.client.lib;

import ru.rmntim.common.commands.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CommandRegistryBuilder {
    private final Map<String, Function<List<String>, Command>> suppliers = new HashMap<>();
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
     * @param commandSupplier function that accepts command args and returns the command,
     *                        or {@code null} if the command is not supposed to be sent to the server
     * @return this, because this is a builder
     */
    public CommandRegistryBuilder register(String name, String description, Function<List<String>, Command> commandSupplier) {
        suppliers.put(name, commandSupplier);
        descriptions.put(name, description);
        return this;
    }

    /**
     * Builds the command registry and adds the `help` and `exit` commands.
     *
     * @return the command registry
     */
    public Map<String, Function<List<String>, Command>> build() {
        register("help", "Show list of commands", args -> {
            if (!args.isEmpty()) {
                throw new IllegalArgumentException("help accepts 0 arguments");
            }
            descriptions.entrySet().forEach(System.out::println);
            return null;
        });

        register("exit", "Exit the program", args -> {
            if (!args.isEmpty()) {
                throw new IllegalArgumentException("exit accepts 0 arguments");
            }
            System.exit(0);
            return null;
        });

        return suppliers;
    }
}
