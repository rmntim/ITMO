package ru.rmntim.client.lib;

import org.apache.commons.lang3.SerializationUtils;
import ru.rmntim.client.network.UDPClient;
import ru.rmntim.common.commands.Command;
import ru.rmntim.common.network.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class REPL {
    private final Map<String, Function<List<String>, Command>> commands;
    private final UDPClient client;

    /**
     * Creates a new REPL.
     *
     * @param commands command registry
     * @param client   UDP client
     */
    public REPL(Map<String, Function<List<String>, Command>> commands, UDPClient client) {
        this.commands = commands;
        this.client = client;
    }

    /**
     * Runs the REPL.
     */
    public void run() {
        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while (true) {
                System.out.print("$ ");

                line = reader.readLine();

                if (line == null) {
                    break;
                }

                var userCommand = Arrays.stream(line.trim().split(" ", 2)).map(String::trim).toList();
                var commandName = userCommand.get(0);

                if (!commands.containsKey(commandName)) {
                    System.out.println("Unknown command: " + commandName);
                    continue;
                }

                Command command;
                try {
                    command = commands.get(commandName).apply(userCommand.subList(1, userCommand.size()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Command error: " + e.getMessage());
                    continue;
                }
                if (command == null) {
                    continue;
                }

                var commandBytes = SerializationUtils.serialize(command);
                var responseBytes = client.sendThenReceive(commandBytes);
                if (responseBytes == null) {
                    System.out.println("Server timeout");
                    continue;
                }

                Response response;
                try {
                    response = SerializationUtils.deserialize(responseBytes);
                } catch (ClassCastException e) {
                    System.out.println("Bad response from server: " + e.getMessage());
                    continue;
                }

                if (response.status() == Response.Status.OK) {
                    System.out.println(response.message());
                } else {
                    System.out.println("Error from server: " + response.message());
                }
            }
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
    }
}
