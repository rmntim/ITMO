package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;

import java.util.List;

public class Info extends Command {
    public static final String NAME = "info";
    public static final String DESCRIPTION = "Prints information about the collection";

    @Override
    public Response accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public static Info create(List<String> args) {
        if (!args.isEmpty()) {
            throw new IllegalArgumentException(NAME + " accepts 0 arguments");
        }
        return new Info();
    }
}
