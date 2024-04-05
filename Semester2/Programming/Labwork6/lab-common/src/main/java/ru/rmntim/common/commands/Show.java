package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;

public class Show extends Command {
    public static final String NAME = "show";
    public static final String DESCRIPTION = "Prints all elements of the collection";

    @Override
    public Response accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
