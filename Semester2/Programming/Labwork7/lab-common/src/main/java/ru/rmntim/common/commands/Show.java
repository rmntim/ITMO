package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;

import java.util.List;

public class Show extends Command {
    public static final String NAME = "show";
    public static final String DESCRIPTION = "Prints all elements of the collection";

    @Override
    public Response accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public static Show create(List<String> args) {
        if (!args.isEmpty()) {
            throw new IllegalArgumentException(NAME + " accepts 0 arguments");
        }
        return new Show();
    }
}
