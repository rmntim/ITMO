package ru.rmntim.common.commands;

import ru.rmntim.common.network.Response;

import java.util.List;

public class Remove extends Command {
    public static final String NAME = "remove_by_id";
    public static final String DESCRIPTION = "Remove element with given id";

    private final int id;

    public Remove(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public Response accept(Command.Visitor visitor) {
        return visitor.visit(this);
    }

    public static Remove create(List<String> args) {
        if (args.size() != 1) {
            throw new IllegalArgumentException(NAME + " needs id as argument");
        }
        try {
            var id = Integer.parseInt(args.get(0));
            return new Remove(id);
        } catch (NumberFormatException e) {
            System.out.println("Id must be a valid integer");
            return null;
        }
    }
}
