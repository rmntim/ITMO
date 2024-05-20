package server.commands;

import common.network.requests.RegisterRequest;
import common.network.requests.Request;
import common.network.responses.RegisterResponse;
import common.network.responses.Response;
import server.managers.AuthManager;

import javax.persistence.PersistenceException;

public class Register extends Command {
    private final AuthManager authManager;
    @SuppressWarnings("FieldCanBeLocal")
    private final int MAX_USERNAME_LENGTH = 40;

    public Register(AuthManager authManager) {
        super("register", "зарегистрировать пользователя");
        this.authManager = authManager;
    }

    @Override
    public Response apply(Request request) {
        var req = (RegisterRequest) request;
        var user = req.getUser();
        if (user.getName().length() >= MAX_USERNAME_LENGTH) {
            return new RegisterResponse(user, "Длина имени пользователя должна быть < " + MAX_USERNAME_LENGTH);
        }

        try {
            var newUserId = authManager.registerUser(user.getName(), user.getPassword());

            if (newUserId <= 0) {
                return new RegisterResponse(user, "Не удалось создать пользователя.");
            } else {
                return new RegisterResponse(user.copy(newUserId), null);
            }
        } catch (IllegalStateException | PersistenceException e) {
            return new RegisterResponse(user, e.toString());
        }
    }
}
