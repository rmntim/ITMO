package ru.rmntim.server.lib;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rmntim.common.commands.Command;
import ru.rmntim.common.network.Response;
import ru.rmntim.server.network.UDPServer;

import java.io.IOException;

public class Interpreter implements Command.Visitor {
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
                var command = (Command) SerializationUtils.deserialize(data.bytes());
                var response = execute(command);
                var responseBytes = SerializationUtils.serialize(response);
                server.send(responseBytes, data.address());
            } catch (IOException e) {
                LOGGER.error("IO error occurred", e);
            }
        }
    }

    /**
     * Executes given command.
     *
     * @param command command to execute
     * @return response from command
     */
    private Response execute(Command command) {
        return command.accept(this);
    }
}