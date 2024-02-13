package ru.rmntim.common.commands;

import ru.rmntim.common.models.Dragon;

import java.util.TreeSet;

public class UpdateCommand extends Command {
    private final TreeSet<Dragon> collection;

    public UpdateCommand(TreeSet<Dragon> collection) {
        super("update [id] {element}", "updates an element in the collection");
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
