package server.network;

import common.network.requests.Request;
import common.network.responses.NoSuchCommandResponse;
import common.network.responses.Response;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import server.App;
import server.handlers.CommandHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

abstract class UDPServer {
    private static final int READ_POOL_SIZE = 4;

    private final InetSocketAddress addr;
    private final CommandHandler commandHandler;
    private final ExecutorService service = Executors.newFixedThreadPool(READ_POOL_SIZE);
    private final ForkJoinPool pool = ForkJoinPool.commonPool();
    private final CountDownLatch latch = new CountDownLatch(1);

    private final Logger logger = App.logger;

    @SuppressWarnings("FieldMayBeFinal")
    private boolean running = true;

    public UDPServer(InetSocketAddress addr, CommandHandler commandHandler) {
        this.addr = addr;
        this.commandHandler = commandHandler;
    }

    public InetSocketAddress getAddr() {
        return addr;
    }

    public abstract Pair<Byte[], SocketAddress> receiveData() throws IOException;

    public abstract void sendData(byte[] data, SocketAddress addr) throws IOException;

    public abstract void connectToClient(SocketAddress addr) throws SocketException;

    public abstract void disconnectFromClient();

    public abstract void close();

    public void run() throws InterruptedException {
        logger.info("Server listening on {}", addr);

        pool.submit(() -> {
            while (running) {
                Pair<Byte[], SocketAddress> dataPair;
                try {
                    dataPair = receiveData();
                } catch (Exception e) {
                    logger.error("Error while receiving data", e);
                    disconnectFromClient();
                    continue;
                }

                var dataFromClient = dataPair.getKey();
                var clientAddr = dataPair.getValue();

                try {
                    connectToClient(clientAddr);
                    logger.info("Connected to {}", clientAddr);
                } catch (Exception e) {
                    logger.error("Error while connecting", e);
                }

                Request request;
                try {
                    request = SerializationUtils.deserialize(ArrayUtils.toPrimitive(dataFromClient));
                    logger.info("Evaluating {} from {}", request, clientAddr);
                } catch (SerializationException e) {
                    logger.error("Serialization error", e);
                    disconnectFromClient();
                    continue;
                }

                pool.submit(() -> {
                    Response response = null;
                    try {
                        response = commandHandler.handle(request);
                    } catch (Exception e) {
                        logger.error("Ошибка выполнения команды", e);
                    }
                    if (response == null) response = new NoSuchCommandResponse(request.getName());

                    var data = SerializationUtils.serialize(response);
                    logger.info("Response: {}", response);

                    service.submit(() -> {
                        try {
                            sendData(data, clientAddr);
                            logger.info("Sent response to {}", clientAddr);
                        } catch (IOException e) {
                            logger.error("IO error", e);
                        }
                    });
                });

                disconnectFromClient();
                logger.info("Disconnected from {}", clientAddr);
            }
            close();
            latch.countDown();
        });
        latch.await();
    }
}
