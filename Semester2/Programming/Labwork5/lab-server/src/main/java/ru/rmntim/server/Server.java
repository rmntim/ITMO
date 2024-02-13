package ru.rmntim.server;

import ru.rmntim.server.storage.JsonStorageManager;

public final class Server {
    private static final String ENV_NAME = "FILENAME";

    private Server() {
        throw new UnsupportedOperationException(
                "This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        var storageManager = new JsonStorageManager(System.getenv(ENV_NAME));
        var collectionManager = new CollectionManager(storageManager);
        collectionManager.validateCollection();
        new InteractiveShell(collectionManager).run();
    }
}
