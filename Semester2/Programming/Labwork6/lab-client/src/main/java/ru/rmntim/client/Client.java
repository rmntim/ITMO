package ru.rmntim.client;

import ru.rmntim.client.lib.CommandRegistryBuilder;
import ru.rmntim.client.lib.REPL;
import ru.rmntim.client.network.UDPClient;
import ru.rmntim.common.GlobalInput;
import ru.rmntim.common.commands.Add;
import ru.rmntim.common.commands.Info;
import ru.rmntim.common.commands.Show;
import ru.rmntim.common.commands.Update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public final class Client {
    private static final Duration TIMEOUT = Duration.of(3, ChronoUnit.SECONDS);
    private static int port = -1;

    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) throws IOException {
        handleArgs(args);

        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
            GlobalInput.setReader(reader);
            var client = new UDPClient(new InetSocketAddress(InetAddress.getLocalHost(), port), TIMEOUT);
            var builder = new CommandRegistryBuilder()
                    .register(Info.NAME, Info.DESCRIPTION, Info::create)
                    .register(Show.NAME, Show.DESCRIPTION, Show::create)
                    .register(Add.NAME, Add.DESCRIPTION, Add::create)
                    .register(Update.NAME, Update.DESCRIPTION, Update::create);
            new REPL(builder, client).run();
        }
    }

    private static void handleArgs(String[] args) {
        if (args.length == 1 && "-h".equals(args[0])) {
            System.out.println("-h        - print this message");
            System.out.println("-p <PORT> - set port to connect to");
            System.exit(0);
        } else if (args.length == 2 && "-p".equals(args[0])) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Port must be a valid number");
                System.exit(1);
            }
        } else if (args.length != 0) {
            System.out.println("Bad argument");
            System.exit(1);
        }

        if (port == -1) {
            System.out.println("Port must be specified");
            System.exit(1);
        }
    }
}
