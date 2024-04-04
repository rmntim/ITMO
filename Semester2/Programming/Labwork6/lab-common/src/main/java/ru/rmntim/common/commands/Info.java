package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;

public class Info extends Command {
    public static final String NAME = "info";
    public static final String DESCRIPTION = "Prints information about the collection";

    @Override
    public Response accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
