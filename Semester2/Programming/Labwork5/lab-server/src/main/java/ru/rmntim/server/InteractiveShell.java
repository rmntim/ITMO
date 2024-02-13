package ru.rmntim.server;

import ru.rmntim.common.commands.CommandParser;
import ru.rmntim.common.commands.StatusCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InteractiveShell {
    private static final Logger LOGGER = Logger.getLogger(InteractiveShell.class.getName());
    private static final String PS1 = ">> ";
    private final Interpreter interpreter;

    public InteractiveShell(final CollectionManager collectionManager) {
        this.interpreter = new Interpreter(collectionManager);
    }

    public void run() {
        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
            StatusCode status = StatusCode.OK;
            String input;
            do {
                System.out.print(PS1);
                input = reader.readLine();
                if (input == null) {
                    System.exit(0);
                }
                if (input.isBlank()) {
                    continue;
                }

                var command = Arrays.stream(input.trim().split(" ", 2))
                        .map(String::trim)
                        .toList();
                status = runCommand(command);
            } while (status != StatusCode.EXIT);
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "Unexpected IO error: " + ioe.getMessage());
        }
    }

    private StatusCode runCommand(final List<String> userCommand) {
        var parser = new CommandParser();
        var command = parser.parse(userCommand);
        var status = command.map(interpreter::execute).orElse(StatusCode.ERROR);
        if (status == StatusCode.ERROR) {
            LOGGER.log(Level.INFO, "Command is not valid");
        }
        return status;
    }
}
