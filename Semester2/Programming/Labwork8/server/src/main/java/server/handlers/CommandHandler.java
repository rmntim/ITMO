package server.handlers;

import common.network.requests.Request;
import common.network.responses.BadCredentialsResponse;
import common.network.responses.ErrorResponse;
import common.network.responses.NoSuchCommandResponse;
import common.network.responses.Response;
import org.slf4j.Logger;
import server.App;
import server.commands.Command;
import server.managers.AuthManager;

import java.sql.SQLException;
import java.util.Map;

public class CommandHandler {
    private final Map<String, Command> commands;
    private final AuthManager authManager;

    private final Logger logger = App.logger;

    public CommandHandler(Map<String, Command> commands, AuthManager authManager) {
        this.commands = commands;
        this.authManager = authManager;
    }

    public Response handle(Request request) {
        if (!request.isAuth()) {
            var user = request.getUser();
            try {
                if (user == null || authManager.authenticateUser(user.getName(), user.getPassword()) <= 0) {
                    return new BadCredentialsResponse("Bad credentials. Please login again.");
                }
            } catch (SQLException e) {
                logger.error("Error while authenticating.", e);
                return new ErrorResponse("sql_error", "Error while authenticating.");
            }
        }

        var command = commands.get(request.getName());
        if (command == null) return new NoSuchCommandResponse(request.getName());
        return command.apply(request);
    }
}
