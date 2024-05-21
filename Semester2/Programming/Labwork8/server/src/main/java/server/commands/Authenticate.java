package server.commands;

import common.network.requests.AuthenticateRequest;
import common.network.requests.Request;
import common.network.responses.AuthenticateResponse;
import common.network.responses.Response;
import server.managers.AuthManager;

public class Authenticate extends Command {
    private final AuthManager authManager;

    public Authenticate(AuthManager authManager) {
        super("authenticate", "аутентифицировать пользователя по логину и паролю");
        this.authManager = authManager;
    }

    @Override
    public Response apply(Request request) {
        var req = (AuthenticateRequest) request;
        var user = req.getUser();
        try {
            var userId = authManager.authenticateUser(user.name(), user.password());

            if (userId <= 0) {
                return new AuthenticateResponse(user, "Нет такого пользователя.");
            } else {
                return new AuthenticateResponse(user.copy(userId), null);
            }
        } catch (Exception e) {
            return new AuthenticateResponse(user, e.toString());
        }
    }
}
