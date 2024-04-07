package ru.rmntim.client;

import ru.rmntim.client.lib.CommandRegistryBuilder;
import ru.rmntim.client.lib.REPL;
import ru.rmntim.client.network.UDPClient;
import ru.rmntim.common.commands.Info;
import ru.rmntim.common.commands.Show;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public final class Client {
    private static final int PORT = 1337;
    private static final Duration TIMEOUT = Duration.of(10, ChronoUnit.SECONDS);

    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) throws IOException {
        var client = new UDPClient(new InetSocketAddress(InetAddress.getLocalHost(), PORT), TIMEOUT);
        var commands = new CommandRegistryBuilder()
                .register(Info.NAME, Info.DESCRIPTION, Info::create)
                .register(Show.NAME, Show.DESCRIPTION, Show::create)
                .build();
        new REPL(commands, client).run();
    }
}
