package ru.rmntim.client;

import org.apache.commons.lang3.SerializationUtils;
import ru.rmntim.client.network.UDPClient;
import ru.rmntim.common.commands.Info;
import ru.rmntim.common.network.Response;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public final class Client {
    private static final int PORT = 1337;

    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) throws IOException {
        var command = new Info();
        var commandBytes = SerializationUtils.serialize(command);

        var client = new UDPClient(new InetSocketAddress(InetAddress.getLocalHost(), PORT));
        var responseBytes = client.sendThenRecieve(commandBytes);
        var response = (Response) SerializationUtils.deserialize(responseBytes);
        System.out.println("Status: " + response.status());
        System.out.println("Message: \n---\n" + response.message() + "\n---");
    }
}
