package ru.rmntim.client;

import ru.rmntim.client.commands.Command;
import ru.rmntim.client.commands.CommandRegistryBuilder;
import ru.rmntim.client.commands.Exit;
import ru.rmntim.client.logic.ExecutionContext;
import ru.rmntim.client.logic.Interpreter;
import ru.rmntim.client.network.UDPClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

public final class Client {
    private static final int PORT = 1337;

    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            var commands = registerCommands();
            var client = new UDPClient(InetAddress.getLocalHost(), PORT);
            var ctx = new ExecutionContext(client, System.in, commands);
            var interpreter = new Interpreter(ctx);

            interpreter.run();
        } catch (IOException e) {
            System.err.println("Couldn't connect to the server. " + e.getMessage());
        }
    }

    private static Map<String, Command> registerCommands() {
        var registryBuilder = new CommandRegistryBuilder();
        registryBuilder.register(new Exit());
        return registryBuilder.build();
    }
}
