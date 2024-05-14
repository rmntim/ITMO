package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;

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
}
