package ru.rmntim.client.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * Manages the connection to the server.
 */
public class UDPClient {
    private static final int PACKET_SIZE = 1024;
    private final SocketAddress serverAddress;
    private final DatagramSocket datagramSocket;

    /**
     * Creates new client.
     *
     * @param serverAddress the address of the server
     * @throws SocketException if socket can't be created
     */
    public UDPClient(SocketAddress serverAddress) throws SocketException {
        this.serverAddress = serverAddress;
        this.datagramSocket = new DatagramSocket();
    }

    /**
     * Sends the command to server and waits for a response.
     *
     * @param data the data to send
     * @return the received data
     * @throws IOException if an I/O error occurs
     */
    public byte[] sendThenRecieve(byte[] data) throws IOException {
        var packet = new DatagramPacket(data, data.length, serverAddress);
        datagramSocket.send(packet);

        var buffer = new byte[PACKET_SIZE];
        var serverPacket = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(serverPacket);

        return serverPacket.getData();
    }
}
