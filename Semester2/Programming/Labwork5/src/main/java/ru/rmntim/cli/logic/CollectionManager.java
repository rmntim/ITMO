package ru.rmntim.cli.logic;

import ru.rmntim.cli.models.Dragon;

import java.time.ZonedDateTime;
import java.util.TreeSet;

public class CollectionManager {
    private final TreeSet<Dragon> collection;
    private final ZonedDateTime initializationDate;
    private final int lastSavedId;

    public CollectionManager(final TreeSet<Dragon> collection, ZonedDateTime initializationDate, int lastSavedId) {
        this.collection = collection;
        this.lastSavedId = lastSavedId;
        this.initializationDate = initializationDate;
    }

    public TreeSet<Dragon> getCollection() {
        return collection;
    }

    public String getCollectionInfo() {
        return "Collection size: " + collection.size() + "\n"
                + "Type: " + collection.getClass().getSimpleName() + "\n"
                + "Initialization date: " + initializationDate;
    }

    public int getLastSavedId() {
        return lastSavedId;
    }
}
