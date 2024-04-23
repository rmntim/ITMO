package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;

import java.util.List;

public class StartsWith extends Command {
    public static final String NAME = "filter_starts_with_name";
    public static final String DESCRIPTION
            = "Displays all elements with name starting with given string";

    private final String prefix;

    public StartsWith(String predicate, UserCredentials userCredentials) {
        super(userCredentials);
        this.prefix = predicate;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public Response accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public static StartsWith create(List<String> args, UserCredentials userCredentials) {
        if (args.size() != 1) {
            throw new IllegalArgumentException(NAME + " needs prefix as argument");
        }
        var argument = args.get(0);
        if (argument.isBlank()) {
            throw new IllegalArgumentException("Prefix cannot be blank");
        }
        return new StartsWith(argument, userCredentials);
    }
}
