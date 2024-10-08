package ru.rmntim.cli;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import ru.rmntim.cli.commands.AddCommand;
import ru.rmntim.cli.commands.AddIfMaxCommand;
import ru.rmntim.cli.commands.AddIfMinCommand;
import ru.rmntim.cli.commands.ClearCommand;
import ru.rmntim.cli.commands.ExecuteCommand;
import ru.rmntim.cli.commands.ExitCommand;
import ru.rmntim.cli.commands.GreaterThanCharacterCommand;
import ru.rmntim.cli.commands.GroupByTypeCommand;
import ru.rmntim.cli.commands.InfoCommand;
import ru.rmntim.cli.commands.RemoveCommand;
import ru.rmntim.cli.commands.RemoveLowerCommand;
import ru.rmntim.cli.commands.SaveCommand;
import ru.rmntim.cli.commands.ShowCommand;
import ru.rmntim.cli.commands.StartsWithNameCommand;
import ru.rmntim.cli.commands.UpdateCommand;
import ru.rmntim.cli.exceptions.ValidationException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.logic.CommandRegistryBuilder;
import ru.rmntim.cli.logic.ExecutionContext;
import ru.rmntim.cli.logic.Interpreter;
import ru.rmntim.cli.storage.JsonStorageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.TreeSet;

public final class Main {
    private static final String ENV_NAME = "FILENAME";

    private Main() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            var storageManager = new JsonStorageManager(getPath());
            var collectionManager = storageManager.readCollection()
                    .orElse(new CollectionManager(new TreeSet<>(), ZonedDateTime.now(), 0));

            var executeCommand = new ExecuteCommand();
            var commandRegistry = new CommandRegistryBuilder()
                    .register(new InfoCommand(collectionManager))
                    .register(new ShowCommand(collectionManager))
                    .register(new AddCommand(collectionManager))
                    .register(new UpdateCommand(collectionManager))
                    .register(new RemoveCommand(collectionManager))
                    .register(new ClearCommand(collectionManager))
                    .register(new SaveCommand(storageManager, collectionManager))
                    .register(executeCommand)
                    .register(new ExitCommand())
                    .register(new AddIfMaxCommand(collectionManager))
                    .register(new AddIfMinCommand(collectionManager))
                    .register(new RemoveLowerCommand(collectionManager))
                    .register(new GroupByTypeCommand(collectionManager))
                    .register(new GreaterThanCharacterCommand(collectionManager))
                    .register(new StartsWithNameCommand(collectionManager))
                    .build();
            executeCommand.setCommands(commandRegistry); // NOTE: костыль
            var reader = new BufferedReader(new InputStreamReader(System.in));
            var ctx = new ExecutionContext(reader, commandRegistry);
            new Interpreter(ctx).run();
        } catch (IOException e) {
            System.err.println("IO error occurred: " + e.getMessage());
        } catch (JsonIOException jioe) {
            System.err.println("Error reading JSON file: " + jioe.getMessage());
        } catch (JsonSyntaxException jse) {
            System.err.println("Error parsing JSON file: " + jse.getMessage());
        } catch (IllegalArgumentException iae) {
            System.err.println("Error creating resource: " + iae.getMessage());
        } catch (ValidationException ve) {
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
