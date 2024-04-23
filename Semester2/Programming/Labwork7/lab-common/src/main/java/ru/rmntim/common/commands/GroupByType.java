package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;
import ru.rmntim.common.network.UserCredentials;

import java.util.List;

public class GroupByType extends Command {
    public static final String NAME = "group_counting_by_type";
    public static final String DESCRIPTION
            = "Groups elements by type and displays number of elements in each group";

    public GroupByType(UserCredentials userCredentials) {
        super(userCredentials);
    }

    @Override
    public Response accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public static GroupByType create(List<String> args, UserCredentials userCredentials) {
        if (!args.isEmpty()) {
            throw new IllegalArgumentException(NAME + " accepts 0 arguments");
        }
        return new GroupByType(userCredentials);
    }
}
