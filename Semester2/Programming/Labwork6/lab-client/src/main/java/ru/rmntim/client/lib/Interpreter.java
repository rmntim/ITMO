package ru.rmntim.client.lib;

import ru.rmntim.client.exceptions.BadCommandArgumentsException;
import ru.rmntim.client.exceptions.BadResponseException;

import java.io.IOException;
import java.util.Arrays;

/**
 * Handles the user input and sends it to the server.
 */
public class Interpreter {
    private final ExecutionContext ctx;

    /**
     * Creates a new interpreter.
     *
     * @param ctx execution context
     */
    public Interpreter(final ExecutionContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Launches the interactive CLI mode.
     */
    public void run() {
        try {
            var commands = ctx.getCommands();
            String input;
            while ((input = ctx.getReader().readLine()) != null) {
                if (!ctx.isInFile()) {
                    System.out.print("> ");
                }

                var userCommand = Arrays.stream(input.trim().split(" ", 2)).map(String::trim).toList();
                var commandName = userCommand.get(0);

                if (!commands.containsKey(commandName)) {
                    System.out.println("Unknown command: " + commandName);
                    continue;
                }

                try {
                    var response = commands.get(commandName)
                            .sendRequest(ctx, userCommand.subList(1, userCommand.size()));
                    if (response.isError()) {
                        System.out.print("Server error: ");
                    }
                    System.out.println(response.message());
                } catch (BadResponseException | BadCommandArgumentsException e) {
                    System.out.println("Error constructing request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Fatal: " + e.getMessage());
        }
    }
}
