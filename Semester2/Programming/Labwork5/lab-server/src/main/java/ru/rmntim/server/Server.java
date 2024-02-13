package ru.rmntim.server;

import ru.rmntim.common.commands.CommandRegistryBuilder;
import ru.rmntim.common.commands.ExitCommand;
import ru.rmntim.common.commands.InfoCommand;
import ru.rmntim.common.commands.ShowCommand;
import ru.rmntim.server.storage.JsonStorageManager;

public final class Server {
    private static final String ENV_NAME = "FILENAME";

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        var storageManager = new JsonStorageManager(System.getenv(ENV_NAME));
        var collectionManager = new CollectionManager(storageManager);
        collectionManager.validateCollection();

        var commandRegistry = new CommandRegistryBuilder()
                .register("exit", new ExitCommand())
                .register("info", new InfoCommand(collectionManager.getCollection(), collectionManager.getInitializationDate()))
                .register("show", new ShowCommand(collectionManager.getCollection()))
                .build();

        new InteractiveShell(commandRegistry).run();
    }
}
