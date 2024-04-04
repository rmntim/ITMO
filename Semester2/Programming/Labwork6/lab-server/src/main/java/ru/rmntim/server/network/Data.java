package ru.rmntim.server.network;

import java.net.SocketAddress;

/**
 * Class for encapsulating data received from the client.
 *
 * @param address the address of the client, from which the data was received
 * @param data    the received data
 */
public record Data(SocketAddress address, byte[] data) {
}
