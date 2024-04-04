package ru.rmntim.server.network;

import java.net.SocketAddress;

/**
 * Class for encapsulating bytes received from the client.
 *
 * @param address the address of the client, from which the bytes was received
 * @param bytes   the received bytes
 */
public record Data(SocketAddress address, byte[] bytes) {
}
