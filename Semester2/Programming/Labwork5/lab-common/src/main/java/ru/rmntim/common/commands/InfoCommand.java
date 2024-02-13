package ru.rmntim.common.commands;

import ru.rmntim.common.models.Dragon;

import java.time.LocalDateTime;
import java.util.TreeSet;

public class InfoCommand extends Command {
    private final TreeSet<Dragon> collection;
    private final LocalDateTime initializationDate;

    public InfoCommand(TreeSet<Dragon> collection, LocalDateTime initializationDate) {
        super("info", "prints information about collection");
        this.collection = collection;
        this.initializationDate = initializationDate;
    }

    public TreeSet<Dragon> getCollection() {
        return collection;
    }

    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }

    @Override
    public <T> T accept(CommandVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
