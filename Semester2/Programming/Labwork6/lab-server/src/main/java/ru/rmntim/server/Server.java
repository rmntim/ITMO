package ru.rmntim.server;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rmntim.common.exceptions.ValidationException;
import ru.rmntim.server.lib.CollectionManager;
import ru.rmntim.server.storage.StorageManager;

import java.io.IOException;

public final class Server {
    private static final int PORT = 1337;
    private static final String ENV_NAME = "FILENAME";
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            var path = getPath();

            logger.info("Loading data from " + path);
            var storageManager = new StorageManager(path);
            var collection = storageManager.readCollection();
            var collectionManager = new CollectionManager(collection);
        } catch (IOException | JsonIOException e) {
            logger.error("IO error occurred", e);
        } catch (ValidationException | JsonSyntaxException e) {
            logger.error("Data validation error occurred", e);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating resource", e);
        } finally {
            logger.info("Server stopped");
        }
    }

    private static String getPath() {
        var path = System.getenv(ENV_NAME);
        if (path == null) {
            logger.error("Environment variable " + ENV_NAME + " is not set");
            System.exit(1);
        }
        return path;
    }
}
