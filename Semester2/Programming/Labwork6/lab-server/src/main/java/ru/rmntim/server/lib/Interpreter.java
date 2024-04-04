package ru.rmntim.server.lib;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rmntim.common.CollectionManager;
import ru.rmntim.common.commands.Command;
import ru.rmntim.server.network.UDPServer;

import java.io.IOException;

public class Interpreter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Interpreter.class);
    private final CollectionManager collectionManager;
    private final UDPServer server;

    /**
     * Creates new interpreter.
     *
     * @param collectionManager the collection
     * @param server            the server implementation
     */
    public Interpreter(CollectionManager collectionManager, UDPServer server) {
        this.collectionManager = collectionManager;
        this.server = server;
    }

    /**
     * Starts the interpreter.
     */
    public void run() {
        while (true) {
            try {
                var data = server.receive();
                var command = Command.parse(data.data());
                var response = command.execute(collectionManager);
                var responseBytes = SerializationUtils.serialize(response);
                server.send(responseBytes, data.address());
            } catch (IOException e) {
                LOGGER.error("IO error occurred", e);
            }
        }
    }
}
