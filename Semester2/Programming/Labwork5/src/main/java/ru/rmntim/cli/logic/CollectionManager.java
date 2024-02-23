package ru.rmntim.cli.logic;

import ru.rmntim.cli.models.Dragon;

import java.time.ZonedDateTime;
import java.util.TreeSet;

public class CollectionManager {
    private final TreeSet<Dragon> collection;
    private final ZonedDateTime initializationDate;

    public CollectionManager(final TreeSet<Dragon> collection) {
        this.collection = collection;
        this.initializationDate = ZonedDateTime.now();
    }

    public TreeSet<Dragon> getCollection() {
        return collection;
    }

    public ZonedDateTime getInitializationDate() {
        return initializationDate;
    }
}
