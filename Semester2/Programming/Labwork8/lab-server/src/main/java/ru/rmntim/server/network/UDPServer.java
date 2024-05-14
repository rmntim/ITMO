package ru.rmntim.server.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPServer {
    private static final int RESPONSE_BYTES_SIZE = 65_536;
    private final DatagramChannel datagramChannel;

    /**
     * Creates new server that binds to given address.
     *
     * @param address server address to bind
     * @throws IOException if server can't be created
     */
    public UDPServer(SocketAddress address) throws IOException {
        datagramChannel = DatagramChannel.open();
        datagramChannel.bind(address);
    }

    /**
     * Gets bytes from client.
     *
     * @return received bytes
     * @throws IOException if bytes can't be received
     */
    public Data receive() throws IOException {
        var buffer = ByteBuffer.allocate(RESPONSE_BYTES_SIZE);
        var clientAddress = datagramChannel.receive(buffer);
        buffer.flip();
        return new Data(clientAddress, buffer);
    }

    /**
     * Sends bytes to the specified socket address.
     *
     * @param data    the byte array of data to be sent
     * @param address the socket address to send the bytes to
     * @throws IOException if an I/O error occurs
     */
    public void send(byte[] data, SocketAddress address) throws IOException {
        var buffer = ByteBuffer.allocate(RESPONSE_BYTES_SIZE);
        buffer.put(data);
        buffer.flip();
        datagramChannel.send(buffer, address);
    }

    /**
     * Gets the port from local socket address.
     *
     * @return port of the socket
     * @throws IOException if an I/O error occurs
     */
    public int getPort() throws IOException {
        return ((InetSocketAddress) datagramChannel.getLocalAddress()).getPort();
    }
}
