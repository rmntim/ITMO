package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;

import java.util.List;

public class Clear extends Command {
    public static final String NAME = "clear";
    public static final String DESCRIPTION = "Clears the collection";

    public Clear(UserCredentials userCredentials) {
        super(userCredentials);
    }

    @Override
    public Response accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public static Clear create(List<String> args, UserCredentials userCredentials) {
        if (!args.isEmpty()) {
            throw new IllegalArgumentException(NAME + " accepts 0 arguments");
        }
        return new Clear(userCredentials);
    }
}
