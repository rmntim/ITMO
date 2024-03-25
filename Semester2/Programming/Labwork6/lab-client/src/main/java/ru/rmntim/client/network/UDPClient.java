package ru.rmntim.client.network;

import com.google.common.primitives.Bytes;
import org.apache.commons.lang3.SerializationUtils;
import ru.rmntim.common.network.requests.Request;
import ru.rmntim.common.network.responses.Response;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

/**
 * Manages the connection to the server.
 */
public class UDPClient {
    private static final int PACKET_SIZE = 1024;

    private final DatagramChannel channel;
    private final InetSocketAddress address;

    /**
     * Creates a new non-blocking connection to the server.
     *
     * @param addr host
     * @param port port
     * @throws IOException if an I/O error occurs
     */
    public UDPClient(final InetAddress addr, int port) throws IOException {
        this.address = new InetSocketAddress(addr, port);
        this.channel = DatagramChannel.open().bind(null).connect(address);
        this.channel.configureBlocking(false);
    }

    /**
     * Sends a request to the server and waits for a response.
     *
     * @param request request to send, cannot be null
     * @return server response
     * @throws IOException if an I/O error occurs
     */
    public Response sendAndReceive(Request request) throws IOException {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        sendRequest(request);
        return recieveResponse();
    }

    private void sendRequest(Request request) throws IOException {
        var requestBytes = SerializationUtils.serialize(request);
        var dataSize = PACKET_SIZE - 1;
        var chunkLength = (int) Math.ceil(requestBytes.length / (double) dataSize);
        var chunks = new byte[chunkLength][dataSize];

        for (int i = 0, start = 0; i < chunks.length; i++, start += dataSize) {
            chunks[i] = Arrays.copyOfRange(requestBytes, start, start + dataSize);
        }

        for (int i = 0; i < chunks.length; i++) {
            var chunk = chunks[i];
            byte[] data;
            if (i != chunks.length - 1) {
                data = Bytes.concat(chunk, new byte[]{0});
            } else {
                data = Bytes.concat(chunk, new byte[]{1});
            }
            channel.send(ByteBuffer.wrap(data), address);
        }
    }

    private Response recieveResponse() throws IOException {
        var finished = false;
        var responseBytes = new byte[0];

        while (!finished) {
            var data = receiveData(PACKET_SIZE);

            if (data[data.length - 1] == 1) {
                finished = true;
            }
            responseBytes = Bytes.concat(responseBytes, Arrays.copyOf(data, data.length - 1));
        }

        return SerializationUtils.deserialize(responseBytes);
    }

    private byte[] receiveData(int size) throws IOException {
        var buffer = ByteBuffer.allocate(size);
        SocketAddress addr = null;
        while (addr == null) {
            addr = channel.receive(buffer);
        }
        return buffer.array();
    }
}
