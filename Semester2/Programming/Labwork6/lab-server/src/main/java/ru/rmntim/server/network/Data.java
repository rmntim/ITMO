package ru.rmntim.server.network;

import java.net.SocketAddress;

public record Data(SocketAddress address, byte[] data) {
}
