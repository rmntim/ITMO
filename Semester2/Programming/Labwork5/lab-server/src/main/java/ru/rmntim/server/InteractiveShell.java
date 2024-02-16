package ru.rmntim.server;

import ru.rmntim.common.ExitException;
import ru.rmntim.common.commands.Command;
import ru.rmntim.server.logic.CollectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

public class InteractiveShell {
    private static final String PS1 = "$ ";

    private final CollectionManager collectionManager;
    private final Map<String, Command> commands;

    public InteractiveShell(final CollectionManager collectionManager, final Map<String, Command> commands) {
        this.collectionManager = collectionManager;
        this.commands = commands;
    }

    public void run() {
        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            for (;;) {
                System.out.print(PS1);
                input = reader.readLine();
                if (input == null) {
                    break;
                }
                var userCommand = Arrays.stream(input.trim().split(" ", 2)).map(String::trim).toList();
                var commandName = userCommand.get(0);
                if (!commands.containsKey(commandName)) {
                    System.err.println("Command doesn't exist: " + commandName);
                    continue;
                }
                commands.get(commandName).execute(userCommand.subList(1, userCommand.size()));
            }
        } catch (IOException ioe) {
            System.err.println("Error getting input: " + ioe.getMessage());
        } catch (ExitException e) {
            System.out.println("Got an exit command");
            System.exit(0);
        }
    }
}
