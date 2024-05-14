package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;

import java.io.Serializable;

/**
 * Base class for all commands.
 */
public abstract class Command implements Serializable {
    private final UserCredentials userCredentials;

    public Command(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public UserCredentials userCredentials() {
        return userCredentials;
    }

    /**
     * Accepts visitor and returns response.
     *
     * @param visitor command visitor
     * @return response
     */
    public abstract Response accept(Visitor visitor);

    public interface Visitor {
        Response visit(Info command);

        Response visit(Show command);

        Response visit(Add command);

        Response visit(Update command);

        Response visit(Remove command);

        Response visit(Clear command);

        Response visit(AddIfMax command);

        Response visit(AddIfMin command);

        Response visit(RemoveLower command);

        Response visit(GroupByType command);

        Response visit(GreaterThanCharacter command);

        Response visit(StartsWith command);
    }
}
