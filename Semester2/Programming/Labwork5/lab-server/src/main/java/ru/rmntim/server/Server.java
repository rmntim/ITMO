package ru.rmntim.server;

import ru.rmntim.common.commands.CommandRegistryBuilder;
import ru.rmntim.common.commands.ExitCommand;
import ru.rmntim.server.storage.JsonStorageManager;

public final class Server {
    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        var storageManager = new JsonStorageManager(System.getenv("FILENAME"));
        var collectionManager = new CollectionManager(storageManager);
        collectionManager.validateCollection();

        var commandRegistry = new CommandRegistryBuilder()
                .register("exit", new ExitCommand())
                .build();

        new InteractiveShell(commandRegistry).run();
    }
}
