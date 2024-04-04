package ru.rmntim.server;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rmntim.common.exceptions.ValidationException;
import ru.rmntim.server.lib.CollectionManager;
import ru.rmntim.server.network.UDPServer;
import ru.rmntim.server.storage.StorageManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public final class Server {
    private static final int PORT = 1337;
    private static final String ENV_NAME = "FILENAME";
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            var path = getPath();
            LOGGER.info("Loading data from {}", path);

            var storageManager = new StorageManager(path);
            var collectionManager = new CollectionManager(storageManager.readCollection());

            Runtime.getRuntime().addShutdownHook(
                    new Thread(() -> {
                        try {
                            storageManager.writeCollection(collectionManager.getCollection());
                        } catch (IOException e) {
                            LOGGER.error("Failed to save collection", e);
                        }
                    }));

            LOGGER.info("Starting server on port " + PORT);
            var server = new UDPServer(new InetSocketAddress(InetAddress.getLocalHost(), PORT));

            new Interpreter(collectionManager, server).run();
        } catch (IOException | JsonIOException e) {
            LOGGER.error("IO error occurred", e);
        } catch (ValidationException | JsonSyntaxException e) {
            LOGGER.error("Data validation error occurred", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error creating resource", e);
        } finally {
            LOGGER.info("Server stopped");
        }
    }

    private static String getPath() {
        var path = System.getenv(ENV_NAME);
        if (path == null) {
            LOGGER.error("Environment variable " + ENV_NAME + " is not set");
            System.exit(1);
        }
        return path;
    }
}
