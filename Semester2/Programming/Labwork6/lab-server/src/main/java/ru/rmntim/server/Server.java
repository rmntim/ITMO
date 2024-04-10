package ru.rmntim.server;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rmntim.common.validators.ValidationException;
import ru.rmntim.server.lib.CollectionManager;
import ru.rmntim.server.lib.Interpreter;
import ru.rmntim.server.network.UDPServer;
import ru.rmntim.server.storage.StorageManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public final class Server {
    private static final String ENV_NAME = "FILENAME";
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static int port = 0;

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            handleArgs(args);
            var path = getPath();
            LOGGER.info("Loading data from {}", path);

            var storageManager = new StorageManager(path);
            var collectionManager = new CollectionManager(storageManager);
            Runtime.getRuntime().addShutdownHook(new Thread(collectionManager::saveCollection));

            var server = new UDPServer(new InetSocketAddress(InetAddress.getLocalHost(), port));
            LOGGER.info("Starting server on port {}", server.getPort());
            var interpreter = new Interpreter(collectionManager, server);
            interpreter.run();
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

    private static void handleArgs(String[] args) {
        if (args.length == 1 && "-h".equals(args[0])) {
            System.out.println("-h        - print this message");
            System.out.println("-p <PORT> - set port to serve on");
            System.exit(0);
        } else if (args.length == 2 && "-p".equals(args[0])) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Port must be a valid number");
                System.exit(1);
            }
        } else if (args.length != 0) {
            System.out.println("Bad argument");
            System.exit(1);
        }
    }
}
