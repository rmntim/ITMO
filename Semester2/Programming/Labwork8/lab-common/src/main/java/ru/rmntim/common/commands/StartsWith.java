package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;

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
}
