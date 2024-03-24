package ru.rmntim.server.logic;

import ru.rmntim.server.exceptions.BadCommandArgumentsException;
import ru.rmntim.server.exceptions.BuildCancelledException;
import ru.rmntim.server.exceptions.ExitException;
import ru.rmntim.server.exceptions.InvalidScriptException;
import ru.rmntim.server.exceptions.RecursionException;

import java.io.IOException;
import java.util.Arrays;

/**
 * Class for interpreting commands.
 * Deals with reading user input, and propagating arguments to commands.
 */
public class Interpreter {
    private static final String PS1 = "$ ";
    private final ExecutionContext context;

    public Interpreter(final ExecutionContext context) {
        this.context = context;
    }

    public void run() {
        try {
            var reader = context.getReader();
            var commands = context.getCommands();

            String input;
            while (true) {
                if (context.isInteractive()) {
                    System.out.print(PS1);
                }
                input = reader.readLine();
                if (input == null) {
                    break;
                }
                var userCommand = Arrays.stream(input.trim().split(" ", 2)).map(String::trim).toList();
                var commandName = userCommand.get(0);
                if (!commands.containsKey(commandName)) {
                    eprintln("Command doesn't exist: " + commandName);
                    continue;
                }
                try {
                    commands.get(commandName).execute(userCommand.subList(1, userCommand.size()), context);
                } catch (BadCommandArgumentsException | RecursionException | InvalidScriptException e) {
                    eprintln(e.getMessage());
                } catch (BuildCancelledException bce) {
                    if (context.isInFile()) {
                        throw bce;
                    }
                    System.out.println("Object build cancelled: " + bce.getMessage());
                }
            }
        } catch (IOException ioe) {
            eprintln("Error getting input: " + ioe.getMessage());
        } catch (ExitException e) {
            System.exit(0);
        }
    }

    private void eprintln(String message) {
        System.out.println("[ERROR]: " + message);
    }
}
