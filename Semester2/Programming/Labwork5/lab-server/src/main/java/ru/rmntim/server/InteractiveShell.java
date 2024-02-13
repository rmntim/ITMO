package ru.rmntim.server;

import ru.rmntim.common.commands.Command;
import ru.rmntim.common.commands.CommandParser;
import ru.rmntim.common.commands.StatusCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InteractiveShell {
    private static final Logger LOGGER = Logger.getLogger(InteractiveShell.class.getName());
    private static final String PS1 = ">> ";
    private final CommandInterpreter interpreter = new CommandInterpreter();
    private final HashMap<String, Command> commands;

    public InteractiveShell(final HashMap<String, Command> commands) {
        this.commands = commands;
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

                var command = Arrays.stream(input.trim().split(" ", 2)).map(String::trim).toList();
                status = runCommand(command);
            } while (status != StatusCode.EXIT);
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "Unexpected IO error: " + ioe.getMessage());
        }
    }

    private StatusCode runCommand(final List<String> userCommand) {
        var commandName = userCommand.get(0);
        if (!commands.containsKey(commandName)) {
            LOGGER.log(Level.INFO, "Command does not exist");
            return StatusCode.ERROR;
        }
        var parser = new CommandParser(commands);
        var command = parser.parse(userCommand).orElseThrow();
        var status = interpreter.execute(command);

        if (status == StatusCode.ERROR) {
            LOGGER.log(Level.INFO, "Command is not valid");
        }
//        if (status == StatusCode.RUN_SCRIPT) {
//            status = runScript(arguments.get(0));
//        }
        return status;
    }

    private StatusCode runScript(final String path) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
