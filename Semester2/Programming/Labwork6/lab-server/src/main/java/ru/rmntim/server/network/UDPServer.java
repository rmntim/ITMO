package ru.rmntim.server.network;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPServer {
    private static final int PACKET_SIZE = 1024;
    private final DatagramChannel datagramChannel;
    private final ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);

    public UDPServer(SocketAddress address) throws IOException {
        datagramChannel = DatagramChannel.open();
        datagramChannel.bind(address);
    }

    public Data receive() throws IOException {
        buffer.clear();
        var clientAddress = datagramChannel.receive(buffer);
        buffer.flip();
        return new Data(clientAddress, buffer.array());
    }

    public void send(byte[] data, SocketAddress address) throws IOException {
        buffer.clear();
        buffer.put(data);
        buffer.flip();
        datagramChannel.send(buffer, address);
    }
}
