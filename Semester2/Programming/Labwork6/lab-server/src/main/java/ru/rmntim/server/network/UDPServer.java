package ru.rmntim.server.network;

import com.google.common.primitives.Bytes;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rmntim.common.network.requests.Request;
import ru.rmntim.common.network.responses.Response;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Arrays;

public class UDPServer {
    private static final int PACKET_SIZE = 1024;
    private static final Logger logger = LoggerFactory.getLogger(UDPServer.class);
    private final DatagramSocket socket;

    /**
     * @param address address to listen on
     * @throws SocketException if binding fails
     */
    public UDPServer(InetSocketAddress address) throws SocketException {
        this.socket = new DatagramSocket(address);
    }

    /**
     * @return received data and client socket
     * @throws IOException if receiving fails
     */
    public Pair<Byte[], SocketAddress> receive() throws IOException {
        var finished = false;
        var buffer = new byte[0];
        SocketAddress addr = null;

        while (!finished) {
            var data = new byte[PACKET_SIZE];

            var dp = new DatagramPacket(data, PACKET_SIZE);
            socket.receive(dp);

            addr = dp.getSocketAddress();
            logger.info("Received " + dp.getLength() + " bytes from " + addr);

            if (data[data.length - 1] == 1) {
                finished = true;
                logger.info("Finished receiving data from " + addr);
            }

            buffer = Bytes.concat(buffer, Arrays.copyOf(data, data.length - 1));
        }
        return new ImmutablePair<>(ArrayUtils.toObject(buffer), addr);
    }

    /**
     * @param data bytes to send
     * @param addr address to send to
     * @throws IOException if sending fails
     */
    public void send(byte[] data, SocketAddress addr) throws IOException {
        var dataSize = PACKET_SIZE - 1;
        var chunkLength = (int) Math.ceil(data.length / (double) dataSize);
        var chunks = new byte[chunkLength][dataSize];

        for (int i = 0, start = 0; i < chunks.length; i++, start += dataSize) {
            chunks[i] = Arrays.copyOfRange(data, start, start + dataSize);
        }

        logger.info("Sending " + chunks.length + " chunks to " + addr);

        for (int i = 0; i < chunks.length; i++) {
            var chunk = chunks[i];
            byte[] sendBytes;
            if (i != chunks.length - 1) {
                sendBytes = Bytes.concat(chunk, new byte[]{0});
            } else {
                sendBytes = Bytes.concat(chunk, new byte[]{1});
            }
            var dp = new DatagramPacket(sendBytes, PACKET_SIZE, addr);
            socket.send(dp);
            logger.info("Sent " + sendBytes.length + " bytes to " + addr);
        }

        logger.info("Finished sending data to " + addr);
    }

    /**
     * Starts the server
     */
    public void run() {
        while (true) {
            Pair<Byte[], SocketAddress> dataPair;
            try {
                dataPair = receive();
            } catch (IOException e) {
                logger.error("Failed to receive data", e);
                disconnect();
                continue;
            }

            var clientAddress = dataPair.getRight();
            try {
                connect(clientAddress);
                logger.info("Connected to " + clientAddress);
            } catch (IOException e) {
                logger.error("Failed to connect to " + clientAddress, e);
                continue;
            }

            var requestBytes = dataPair.getLeft();
            Request request;
            try {
                request = SerializationUtils.deserialize(ArrayUtils.toPrimitive(requestBytes));
                logger.info("Received request " + request.getName() + " from " + clientAddress);
            } catch (SerializationException e) {
                logger.error("Failed to deserialize request", e);
                disconnect();
                continue;
            }

            Response response;
            try {
                // TODO: handle request
            } catch (Exception e) {
                logger.error("Failed to handle request", e);
            }

            try {
                var responseBytes = SerializationUtils.serialize(response);
                send(responseBytes, clientAddress);
                logger.info("Sending response " + response + " to " + clientAddress);
            } catch (IOException e) {
                logger.error("Failed to send response", e);
            }
            disconnect();
            logger.info("Disconnected from " + clientAddress);
        }
        close();
    }

    private void connect(SocketAddress addr) throws IOException {
        socket.connect(addr);
    }

    private void disconnect() {
        socket.disconnect();
    }

    private void close() {
        socket.close();
    }
}
