package ru.rmntim.server.network;

import java.net.InetSocketAddress;

public class UDPServer {
    private final InetSocketAddress address;

    public UDPServer(InetSocketAddress address) {
        this.address = address;
    }
}
