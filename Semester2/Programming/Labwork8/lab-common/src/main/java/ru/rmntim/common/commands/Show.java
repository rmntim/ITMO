package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;

public class Show extends Command {
    public static final String NAME = "show";
    public static final String DESCRIPTION = "Prints all elements of the collection";

    public Show(UserCredentials userCredentials) {
        super(userCredentials);
    }

    @Override
    public Response accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
