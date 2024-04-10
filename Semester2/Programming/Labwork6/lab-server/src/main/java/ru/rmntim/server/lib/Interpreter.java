package ru.rmntim.server.lib;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rmntim.common.commands.Add;
import ru.rmntim.common.commands.AddIfMax;
import ru.rmntim.common.commands.AddIfMin;
import ru.rmntim.common.commands.Clear;
import ru.rmntim.common.commands.Command;
import ru.rmntim.common.commands.GroupByType;
import ru.rmntim.common.commands.Info;
import ru.rmntim.common.commands.Remove;
import ru.rmntim.common.commands.RemoveLower;
import ru.rmntim.common.commands.Show;
import ru.rmntim.common.commands.Update;
import ru.rmntim.common.network.Response;
import ru.rmntim.common.validators.ValidationException;
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
                LOGGER.info("Received a command from {} (length: {})", data.address(), data.bytes().length);

                Command command;
                try {
                    command = SerializationUtils.deserialize(data.bytes());
                } catch (ClassCastException e) {
                    LOGGER.error("Invalid command received from {}", data.address());
                    continue;
                }

                var response = execute(command);

                var responseBytes = SerializationUtils.serialize(response);
                server.send(responseBytes, data.address());

                LOGGER.info("Sent response to {} (length: {})", data.address(), responseBytes.length);
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

    @Override
    public Response visit(Info command) {
        return new Response(Response.Status.OK, collectionManager.getCollectionInfo());
    }

    @Override
    public Response visit(Show command) {
        return new Response(Response.Status.OK, collectionManager.collectionString());
    }

    @Override
    public Response visit(Add command) {
        try {
            collectionManager.add(command.getDragon());
            return new Response(Response.Status.OK, "Added successfully with id " + collectionManager.getLastId());
        } catch (ValidationException e) {
            return new Response(Response.Status.ERROR, "Invalid element: " + e.getMessage());
        }
    }

    @Override
    public Response visit(Update command) {
        try {
            if (!collectionManager.update(command.getId(), command.getDragon())) {
                return new Response(Response.Status.ERROR, "No elements were updated (maybe id is incorrect?)");
            }
            return new Response(Response.Status.OK, "Updated successfully");
        } catch (ValidationException e) {
            return new Response(Response.Status.ERROR, "Invalid element: " + e.getMessage());
        }
    }

    @Override
    public Response visit(Remove command) {
        if (!collectionManager.remove(command.getId())) {
            return new Response(Response.Status.ERROR, "No elements were removed (maybe id is incorrect?)");
        }
        return new Response(Response.Status.OK, "Removed successfully");
    }

    @Override
    public Response visit(Clear command) {
        collectionManager.clear();
        return new Response(Response.Status.OK, "Cleared successfully");
    }

    @Override
    public Response visit(AddIfMax command) {
        try {
            collectionManager.addIfMax(command.getDragon());
            return new Response(Response.Status.OK, "Command executed successfully");
        } catch (ValidationException e) {
            return new Response(Response.Status.ERROR, "Invalid element: " + e.getMessage());
        }
    }

    @Override
    public Response visit(AddIfMin command) {
        try {
            collectionManager.addIfMin(command.getDragon());
            return new Response(Response.Status.OK, "Command executed successfully");
        } catch (ValidationException e) {
            return new Response(Response.Status.ERROR, "Invalid element: " + e.getMessage());
        }
    }

    @Override
    public Response visit(RemoveLower command) {
        try {
            collectionManager.removeIfLower(command.getDragon());
            return new Response(Response.Status.OK, "Command executed successfully");
        } catch (ValidationException e) {
            return new Response(Response.Status.ERROR, "Invalid element: " + e.getMessage());
        }
    }

    @Override
    public Response visit(GroupByType command) {
        return new Response(Response.Status.OK, collectionManager.groupByType());
    }
}
