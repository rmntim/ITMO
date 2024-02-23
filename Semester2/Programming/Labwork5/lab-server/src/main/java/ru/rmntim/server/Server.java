package ru.rmntim.server;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import ru.rmntim.server.commands.ExitCommand;
import ru.rmntim.server.logic.CollectionManager;
import ru.rmntim.server.logic.CommandRegistryBuilder;
import ru.rmntim.server.storage.JsonStorageManager;

import java.io.IOException;
import java.util.TreeSet;

public final class Server {
    private static final String ENV_NAME = "FILENAME";

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            var storageManager = new JsonStorageManager(System.getenv(ENV_NAME));
            var collection = storageManager.readCollection();
            collection = collection == null ? new TreeSet<>() : collection;
            var collectionManager = new CollectionManager(collection);
            var commandRegistry = new CommandRegistryBuilder()
                    .register(new ExitCommand())
                    .create();
            new InteractiveShell(commandRegistry).run();
        } catch (IOException e) {
            System.err.println("IO error occurred: " + e.getMessage());
        } catch (JsonIOException jioe) {
            System.err.println("Error reading JSON file: " + jioe.getMessage());
        } catch (JsonSyntaxException jse) {
            System.err.println("Error parsing JSON file: " + jse.getMessage());
        } catch (IllegalArgumentException iae) {
            System.err.println("Error creating resource: " + iae.getMessage());
        }
    }
}
