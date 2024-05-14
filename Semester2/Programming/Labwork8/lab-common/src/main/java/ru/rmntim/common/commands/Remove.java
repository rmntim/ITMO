package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;

public class Remove extends Command {
    public static final String NAME = "remove_by_id";
    public static final String DESCRIPTION = "Remove element with given id";

    private final int id;

    public Remove(int id, UserCredentials userCredentials) {
        super(userCredentials);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public Response accept(Command.Visitor visitor) {
        return visitor.visit(this);
    }
}
