package ru.rmntim.cli;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import ru.rmntim.cli.commands.ExitCommand;
import ru.rmntim.cli.commands.InfoCommand;
import ru.rmntim.cli.commands.ShowCommand;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.logic.CommandRegistryBuilder;
import ru.rmntim.cli.storage.JsonStorageManager;

import java.io.IOException;
import java.util.TreeSet;

public final class Main {
    private static final String ENV_NAME = "FILENAME";

    private Main() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            var storageManager = new JsonStorageManager(System.getenv(ENV_NAME));
            var collection = storageManager.readCollection();
            collection = collection == null ? new TreeSet<>() : collection;
            var collectionManager = new CollectionManager(collection);
            var commandRegistry = new CommandRegistryBuilder()
                    .register(new InfoCommand(collectionManager))
                    .register(new ShowCommand(collectionManager))
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
