package ru.rmntim.server;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.TreeSet;

public final class Server {
    private static final String ENV_NAME = "FILENAME";

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }
    public static void main(String[] args) {
        try {
            var storageManager = new ru.rmntim.server.storage.JsonStorageManager(getPath());
            var collectionManager = storageManager.readCollection()
                    .orElse(new ru.rmntim.server.logic.CollectionManager(new TreeSet<>(), ZonedDateTime.now(), 0));

            var executeCommand = new ru.rmntim.server.commands.ExecuteCommand();
            var commandRegistry = new ru.rmntim.server.logic.CommandRegistryBuilder()
                    .register(new ru.rmntim.server.commands.InfoCommand(collectionManager))
                    .register(new ru.rmntim.server.commands.ShowCommand(collectionManager))
                    .register(new ru.rmntim.server.commands.AddCommand(collectionManager))
                    .register(new ru.rmntim.server.commands.UpdateCommand(collectionManager))
                    .register(new ru.rmntim.server.commands.RemoveCommand(collectionManager))
                    .register(new ru.rmntim.server.commands.ClearCommand(collectionManager))
                    .register(new ru.rmntim.server.commands.SaveCommand(storageManager, collectionManager))
                    .register(executeCommand)
                    .register(new ru.rmntim.server.commands.ExitCommand())
                    .register(new ru.rmntim.server.commands.AddIfMaxCommand(collectionManager))
                    .register(new ru.rmntim.server.commands.AddIfMinCommand(collectionManager))
                    .register(new ru.rmntim.server.commands.RemoveLowerCommand(collectionManager))
                    .register(new ru.rmntim.server.commands.GroupByTypeCommand(collectionManager))
                    .register(new ru.rmntim.server.commands.GreaterThanCharacterCommand(collectionManager))
                    .register(new ru.rmntim.server.commands.StartsWithNameCommand(collectionManager))
                    .build();
            executeCommand.setCommands(commandRegistry); // NOTE: костыль
            var reader = new BufferedReader(new InputStreamReader(System.in));
            var ctx = new ru.rmntim.server.logic.ExecutionContext(reader, commandRegistry);
            new ru.rmntim.server.logic.Interpreter(ctx).run();
        } catch (IOException e) {
            System.err.println("IO error occurred: " + e.getMessage());
        } catch (JsonIOException jioe) {
            System.err.println("Error reading JSON file: " + jioe.getMessage());
        } catch (JsonSyntaxException jse) {
            System.err.println("Error parsing JSON file: " + jse.getMessage());
        } catch (IllegalArgumentException iae) {
            System.err.println("Error creating resource: " + iae.getMessage());
        } catch (ru.rmntim.server.exceptions.ValidationException ve) {
            System.err.println("Collection is invalid: " + ve.getMessage());
        }
    }

    private static String getPath() {
        var path = System.getenv(ENV_NAME);
        if (path == null) {
            System.err.println(ENV_NAME + " is null");
            System.exit(1);
        }
        return path;
    }
}
