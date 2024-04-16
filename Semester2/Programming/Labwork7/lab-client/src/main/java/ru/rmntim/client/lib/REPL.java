package ru.rmntim.client.lib;

import org.apache.commons.lang3.SerializationUtils;
import ru.rmntim.client.network.UDPClient;
import ru.rmntim.common.GlobalInput;
import ru.rmntim.common.commands.Command;
import ru.rmntim.common.network.Request;
import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;
import ru.rmntim.common.parsers.BuildCancelledException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class REPL {
    // This execute_script implementation is shit. Beware!
    private static final String EXECUTE_SCRIPT_NAME = "execute_script";
    private static final String EXECUTE_SCRIPT_DESCRIPTION = "Executes script from given file";
    private final Set<String> callStack = new HashSet<>();
    private final Map<String, Function<List<String>, Command>> commands;
    private final UDPClient client;
    private UserCredentials userCredentials;

    /**
     * Creates a new REPL.
     *
     * @param builder registry builder
     * @param client  UDP client
     */
    public REPL(CommandRegistryBuilder builder, UDPClient client) {
        builder.register(EXECUTE_SCRIPT_NAME, EXECUTE_SCRIPT_DESCRIPTION, args -> null);
        this.client = client;
        this.commands = builder.build();
    }

    /**
     * Runs the REPL.
     */
    public void run() {
        try {
            var reader = GlobalInput.getReader();
            askForCredentials();
            String line;
            while (true) {
                System.out.print("$ ");

                line = reader.readLine();
                if (line == null) {
                    break;
                }

                var userCommand = Arrays.stream(line.trim().split(" ", 2)).map(String::trim).toList();
                var commandName = userCommand.get(0);
                var commandArgs = userCommand.subList(1, userCommand.size());

                if (EXECUTE_SCRIPT_NAME.equals(commandName)) {
                    if (commandArgs.size() != 1) {
                        System.out.println(EXECUTE_SCRIPT_NAME + " requires file path");
                    } else {
                        var oldReader = GlobalInput.getReader();
                        try {
                            GlobalInput.setInFile(true);
                            runFile(commandArgs.get(0));
                        } finally {
                            GlobalInput.setReader(oldReader);
                            GlobalInput.setInFile(false);
                        }
                    }
                    continue;
                }

                Response response;
                try {
                    var result = runCommand(commandName, commandArgs);
                    if (result.isEmpty()) {
                        continue;
                    }
                    response = result.get();
                } catch (IOException e) {
                    System.out.println("Command error: " + e.getMessage());
                    continue;
                } catch (BuildCancelledException e) {
                    System.out.println("Build cancelled");
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

    /**
     * Reads credentials from stdin.
     *
     * @throws IOException if failed to read credentials
     */
    private void askForCredentials() throws IOException {
        var reader = GlobalInput.getReader();
        System.out.print("Enter username: ");
        var username = reader.readLine();
        if (username == null) {
            throw new IOException("Failed to read username");
        }
        System.out.print("Enter password: ");
        var password = reader.readLine();
        if (password == null) {
            throw new IOException("Failed to read password");
        }
        userCredentials = new UserCredentials(username, password);
    }

    /**
     * Executes script from given file path.
     *
     * @param filePath path to file to execute
     */
    private void runFile(String filePath) {
        var file = new File(filePath);

        if (!file.isFile() || !file.canRead()) {
            System.out.println("Invalid file");
            return;
        }

        if (callStack.contains(file.getAbsolutePath())) {
            System.out.println("Recursion detected");
            return;
        }

        try (var reader = new BufferedReader(new FileReader(file))) {
            GlobalInput.setReader(reader);
            callStack.add(file.getAbsolutePath());
            String line;
            while (true) {
                line = reader.readLine();

                if (line == null) {
                    break;
                }

                var userCommand = Arrays.stream(line.trim().split(" ", 2)).map(String::trim).toList();
                var commandName = userCommand.get(0);
                var commandArgs = userCommand.subList(1, userCommand.size());

                if (EXECUTE_SCRIPT_NAME.equals(commandName)) {
                    if (commandArgs.size() != 1) {
                        System.out.println(EXECUTE_SCRIPT_NAME + " requires file path");
                    } else {
                        runFile(commandArgs.get(0));
                    }
                    continue;
                }

                Response response;
                try {
                    var result = runCommand(commandName, commandArgs);
                    if (result.isEmpty()) {
                        continue;
                    }
                    response = result.get();
                } catch (IOException e) {
                    System.out.println("Command error: " + e.getMessage());
                    continue;
                } catch (BuildCancelledException e) {
                    System.out.println("Build cancelled");
                    continue;
                }

                if (response.status() == Response.Status.OK) {
                    System.out.println(response.message());
                } else {
                    System.out.println("Error from server: " + response.message());
                }
            }
        } catch (IOException e) {
            System.out.println("IO error in script: " + e.getMessage());
        } finally {
            callStack.remove(file.getAbsolutePath());
        }
    }

    /**
     * Runs given command.
     *
     * @param commandName command name
     * @param commandArgs list of command arguments
     * @return server response or empty optional if there was an error
     * @throws IOException if an I/O exception occurs
     */
    private Optional<Response> runCommand(String commandName, List<String> commandArgs) throws IOException {
        if (!commands.containsKey(commandName)) {
            System.out.println("Unknown command: " + commandName);
            return Optional.empty();
        }

        Command command;
        try {
            command = commands.get(commandName).apply(commandArgs);
        } catch (IllegalArgumentException e) {
            System.out.println("Command error: " + e.getMessage());
            return Optional.empty();
        }
        if (command == null) {
            return Optional.empty();
        }

        var request = new Request(userCredentials, command);
        var requestBytes = SerializationUtils.serialize(request);
        var responseBytes = client.sendThenReceive(requestBytes);
        if (responseBytes == null) {
            System.out.println("Server timeout");
            return Optional.empty();
        }

        Response response;
        try {
            response = SerializationUtils.deserialize(responseBytes);
        } catch (ClassCastException e) {
            System.out.println("Bad response from server: " + e.getMessage());
            return Optional.empty();
        }
        return Optional.of(response);
    }
}
