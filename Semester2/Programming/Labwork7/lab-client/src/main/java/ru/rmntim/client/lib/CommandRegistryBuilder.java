package ru.rmntim.client.lib;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistryBuilder {
    private final Map<String, CommandCreator> suppliers = new HashMap<>();
    private final Map<String, String> descriptions = new HashMap<>();

    /**
     * Class used for managing available commands.
     */
    public CommandRegistryBuilder() {
    }

    /**
     * Adds a command to the registry.
     *
     * @param name           name of the command
     * @param description    description of the command
     * @param commandCreator function that accepts command args, user credentials and returns the command,
     *                       or {@code null} if the command is not supposed to be sent to the server
     * @return this, because this is a builder
     */
    public CommandRegistryBuilder register(String name, String description, CommandCreator commandCreator) {
        suppliers.put(name, commandCreator);
        descriptions.put(name, description);
        return this;
    }

    /**
     * Builds the command registry and adds the `help` and `exit` commands.
     *
     * @return the command registry
     */
    public Map<String, CommandCreator> build() {
        register("help", "Show list of commands", (args, creds) -> {
            if (!args.isEmpty()) {
                throw new IllegalArgumentException("help accepts 0 arguments");
            }
            descriptions.entrySet().forEach(System.out::println);
            return null;
        });

        register("exit", "Exit the program", (args, creds) -> {
            if (!args.isEmpty()) {
                throw new IllegalArgumentException("exit accepts 0 arguments");
            }
            System.exit(0);
            return null;
        });

        return suppliers;
    }
}
