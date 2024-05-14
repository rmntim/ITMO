package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;

import java.util.List;

public class Info extends Command {
    public static final String NAME = "info";
    public static final String DESCRIPTION = "Prints information about the collection";

    public Info(UserCredentials userCredentials) {
        super(userCredentials);
    }

    @Override
    public Response accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public static Info create(List<String> args, UserCredentials userCredentials) {
        if (!args.isEmpty()) {
            throw new IllegalArgumentException(NAME + " accepts 0 arguments");
        }
        return new Info(userCredentials);
    }
}
