package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;

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
}
