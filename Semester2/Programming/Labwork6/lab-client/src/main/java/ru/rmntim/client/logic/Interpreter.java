package ru.rmntim.client.logic;

import ru.rmntim.client.exceptions.BadCommandArgumentsException;
import ru.rmntim.client.exceptions.BadResponseException;
import ru.rmntim.common.network.responses.Response;

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
        var reader = ctx.getReader();
        var commands = ctx.getCommands();

        try {
            String input;

            while (true) {
                if (!ctx.isInFile()) {
                    System.out.print("> ");
                }

                input = reader.readLine();
                if (input == null) {
                    break;
                }
                var userCommand = Arrays.stream(input.trim().split(" ", 2)).map(String::trim).toList();
                var commandName = userCommand.get(0);

                if (!commands.containsKey(commandName)) {
                    System.out.println("Unknown command: " + commandName);
                    continue;
                }

                Response response;
                try {
                    response = commands.get(commandName)
                            .sendRequest(ctx, userCommand.subList(1, userCommand.size()));
                } catch (BadResponseException e) {
                    System.out.println("Response error: " + e.getMessage());
                    continue;
                }

                if (response.isError()) {
                    System.out.print("Request error: ");
                } else {
                    System.out.print("Request success: ");
                }
                System.out.println(response.message());
            }
        } catch (IOException | BadCommandArgumentsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
