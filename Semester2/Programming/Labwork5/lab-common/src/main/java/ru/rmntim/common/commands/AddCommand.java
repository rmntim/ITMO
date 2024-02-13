package ru.rmntim.common.commands;

import ru.rmntim.common.models.Dragon;

import java.util.TreeSet;

public class AddCommand extends Command {
    private final TreeSet<Dragon> collection;

    public AddCommand(TreeSet<Dragon> collection) {
        super("add {element}", "adds an element to the collection");
        this.collection = collection;
    }

    public TreeSet<Dragon> getCollection() {
        return collection;
    }

    @Override
    public <T> T accept(CommandVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
