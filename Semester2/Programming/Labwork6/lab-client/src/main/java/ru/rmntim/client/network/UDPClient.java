package ru.rmntim.client.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

/**
 * Manages the connection to the server.
 */
public class UDPClient {
    private final DatagramChannel channel;
    private final InetSocketAddress address;

    /**
     * Creates a new non-blocking connection to the server.
     * @param addr host
     * @param port port
     * @throws IOException if an I/O error occurs
     */
    public UDPClient(final InetAddress addr, int port) throws IOException {
        this.address = new InetSocketAddress(addr, port);
        this.channel = DatagramChannel.open().bind(null).connect(address);
        this.channel.configureBlocking(false);
    }
}
