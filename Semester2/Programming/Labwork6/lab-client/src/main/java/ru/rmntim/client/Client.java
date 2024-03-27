package ru.rmntim.client;

import ru.rmntim.client.commands.Command;
import ru.rmntim.client.commands.Exit;
import ru.rmntim.client.lib.CommandRegistryBuilder;
import ru.rmntim.client.lib.ExecutionContext;
import ru.rmntim.client.lib.Interpreter;
import ru.rmntim.client.network.UDPClient;

import java.util.Map;

public final class Client {
    private static final int PORT = 1337;

    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        var commands = registerCommands();
        var client = new UDPClient(PORT);
        var ctx = new ExecutionContext(client, System.in, commands);
        var interpreter = new Interpreter(ctx);

        interpreter.run();
    }

    private static Map<String, Command> registerCommands() {
        var registryBuilder = new CommandRegistryBuilder();
        return registryBuilder.register(new Exit())
                .build();
    }
}
