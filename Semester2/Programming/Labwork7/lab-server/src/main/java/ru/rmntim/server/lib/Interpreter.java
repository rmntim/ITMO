package ru.rmntim.server.lib;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rmntim.common.commands.Add;
import ru.rmntim.common.commands.AddIfMax;
import ru.rmntim.common.commands.AddIfMin;
import ru.rmntim.common.commands.Clear;
import ru.rmntim.common.commands.Command;
import ru.rmntim.common.commands.GreaterThanCharacter;
import ru.rmntim.common.commands.GroupByType;
import ru.rmntim.common.commands.Info;
import ru.rmntim.common.commands.Remove;
import ru.rmntim.common.commands.RemoveLower;
import ru.rmntim.common.commands.Show;
import ru.rmntim.common.commands.StartsWith;
import ru.rmntim.common.commands.Update;
import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;
import ru.rmntim.common.validators.ValidationException;
import ru.rmntim.server.network.UDPServer;
import ru.rmntim.server.storage.User;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Interpreter implements Command.Visitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Interpreter.class);
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
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
        parseRequestMultithreaded();
    }

    /**
     * Parses request from socket and deserializes it.
     */
    private void parseRequestMultithreaded() {
        forkJoinPool.execute(() -> {
            while (true) {
                try {
                    var result = parseRequest();
                    if (result == null) {
                        return;
                    }
                    executeMultithreaded(result.getKey(), result.getValue());
                } catch (IOException e) {
                    LOGGER.error("IO error occurred", e);
                }
            }
        });
    }

    /**
     * Executes given command.
     *
     * @param addr    client address
     * @param command command to execute
     */
    private void executeMultithreaded(SocketAddress addr, Command command) {
        forkJoinPool.execute(() -> {
            try {
                var response = executeCommand(command, addr);
                LOGGER.info("Response ready for {} (status: {})", addr, response.status().toString());
                sendMultithreaded(addr, response);
            } catch (IOException e) {
                LOGGER.error("IO error occurred", e);
            }
        });
    }

    /**
     * Sends response to client.
     *
     * @param addr     client address
     * @param response response to send
     */
    private void sendMultithreaded(SocketAddress addr, Response response) {
        executorService.execute(() -> {
            try {
                sendResponse(addr, response);
            } catch (IOException e) {
                LOGGER.error("IO error occurred", e);
            }
        });
    }

    /**
     * Sends response to client.
     *
     * @param response response to send
     * @param addr     client address
     * @throws IOException if response can't be sent
     */
    private void sendResponse(SocketAddress addr, Response response) throws IOException {
        var responseBytes = SerializationUtils.serialize(response);
        server.send(responseBytes, addr);
        LOGGER.info("Sent response to {} (length: {})", addr, responseBytes.length);
    }

    /**
     * Performs command execution.
     *
     * @param command command to execute
     * @param addr    client address
     * @return response
     * @throws IOException if response can't be sent
     */
    private Response executeCommand(Command command, SocketAddress addr) throws IOException {
        Response response;
        try {
            checkCredentials(command.userCredentials());
            response = execute(command);
        } catch (BadCredentialsException e) {
            LOGGER.error("Bad credentials received from {}", addr);
            response = new Response(Response.Status.BAD_CREDENTIALS, "Invalid credentials");
        }
        return response;
    }

    /**
     * Reads request from socket and deserializes it.
     *
     * @return pair of socket address and command
     * @throws IOException if bytes can't be received
     */
    private Pair<SocketAddress, Command> parseRequest() throws IOException {
        var data = server.receive();
        LOGGER.info("Received data from {} (length: {})", data.address(), data.bytes().remaining());

        Command command;
        try {
            command = SerializationUtils.deserialize(data.bytes().array());
            LOGGER.info("Received request with command type: {}", command.getClass().getSimpleName());
        } catch (ClassCastException e) {
            LOGGER.error("Invalid command received from {}", data.address());
            return null;
        }

        return Pair.of(data.address(), command);
    }

    /**
     * Checks user credentials.
     *
     * @param userCredentials credentials to check
     * @throws BadCredentialsException if credentials are incorrect
     */
    private void checkCredentials(UserCredentials userCredentials) {
        var username = userCredentials.username();
        var passwordBytes = userCredentials.password().getBytes(StandardCharsets.UTF_8);
        try {
            var hash = MessageDigest.getInstance("MD5");
            var passwordHash = hash.digest(passwordBytes);
            var optionalUser = collectionManager.getUser(username);
            User user;
            if (optionalUser.isEmpty()) {
                user = collectionManager.registerUser(userCredentials);
            } else {
                user = optionalUser.get();
            }
            if (!Arrays.equals(passwordHash, user.passwordHash())) {
                throw new BadCredentialsException("Invalid password");
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("No such algorithm", e);
        } catch (SQLException e) {
            LOGGER.error("Couldn't register user", e);
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
            var id = collectionManager.add(command.getDragon(), command.userCredentials().username());
            return new Response(Response.Status.OK, "Added successfully with id " + id);
        } catch (ValidationException e) {
            return new Response(Response.Status.ERROR, "Invalid element: " + e.getMessage());
        } catch (SQLException e) {
            return new Response(Response.Status.ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    public Response visit(Update command) {
        try {
            collectionManager.update(command.getId(), command.getDragon(), command.userCredentials().username());
            return new Response(Response.Status.OK, "Updated successfully");
        } catch (ValidationException e) {
            return new Response(Response.Status.ERROR, "Invalid element: " + e.getMessage());
        } catch (SQLException e) {
            return new Response(Response.Status.ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    public Response visit(Remove command) {
        try {
            collectionManager.remove(command.getId(), command.userCredentials().username());
        } catch (SQLException e) {
            return new Response(Response.Status.ERROR, "Database error: " + e.getMessage());
        }
        return new Response(Response.Status.OK, "Removed successfully");
    }

    @Override
    public Response visit(Clear command) {
        try {
            collectionManager.clear(command.userCredentials().username());
        } catch (SQLException e) {
            return new Response(Response.Status.ERROR, "Database error: " + e.getMessage());
        }
        return new Response(Response.Status.OK, "Cleared successfully");
    }

    @Override
    public Response visit(AddIfMax command) {
        try {
            collectionManager.addIfMax(command.getDragon(), command.userCredentials().username());
            return new Response(Response.Status.OK, "Command executed successfully");
        } catch (ValidationException e) {
            return new Response(Response.Status.ERROR, "Invalid element: " + e.getMessage());
        } catch (SQLException e) {
            return new Response(Response.Status.ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    public Response visit(AddIfMin command) {
        try {
            collectionManager.addIfMin(command.getDragon(), command.userCredentials().username());
            return new Response(Response.Status.OK, "Command executed successfully");
        } catch (ValidationException e) {
            return new Response(Response.Status.ERROR, "Invalid element: " + e.getMessage());
        } catch (SQLException e) {
            return new Response(Response.Status.ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    public Response visit(RemoveLower command) {
        try {
            collectionManager.removeIfLower(command.getDragon(), command.userCredentials().username());
            return new Response(Response.Status.OK, "Command executed successfully");
        } catch (ValidationException e) {
            return new Response(Response.Status.ERROR, "Invalid element: " + e.getMessage());
        } catch (SQLException e) {
            return new Response(Response.Status.ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    public Response visit(GroupByType command) {
        var sj = new StringJoiner("\n");
        collectionManager.groupByType()
                .forEach((key, value) -> sj.add(key + ": " + value));
        return new Response(Response.Status.OK, sj.toString());
    }

    @Override
    public Response visit(GreaterThanCharacter command) {
        try {
            return new Response(Response.Status.OK, Long.toString(collectionManager.greaterThanCharacter(command.getDragon())));
        } catch (ValidationException e) {
            return new Response(Response.Status.ERROR, "Invalid element: " + e.getMessage());
        }
    }

    @Override
    public Response visit(StartsWith command) {
        var sj = new StringJoiner("\n");
        collectionManager.startsWith(command.getPrefix())
                .forEach(e -> sj.add(e.toString()));
        return new Response(Response.Status.OK, sj.toString());
    }
}
