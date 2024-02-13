package ru.rmntim.common.commands;

import ru.rmntim.common.models.Dragon;

import java.util.TreeSet;

public class ShowCommand extends Command {
    private final TreeSet<Dragon> collection;

    public ShowCommand(TreeSet<Dragon> collection) {
        super("show", "displays all elements of the collection");
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
