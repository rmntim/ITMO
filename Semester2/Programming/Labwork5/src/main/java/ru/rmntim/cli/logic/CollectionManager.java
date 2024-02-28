package ru.rmntim.cli.logic;

import ru.rmntim.cli.models.Dragon;

import java.time.ZonedDateTime;
import java.util.TreeSet;

public class CollectionManager {
    private final TreeSet<Dragon> collection;
    private final ZonedDateTime initializationDate;
    private int lastSavedId;

    public CollectionManager(final TreeSet<Dragon> collection, ZonedDateTime initializationDate, int lastSavedId) {
        this.collection = collection;
        this.lastSavedId = lastSavedId;
        this.initializationDate = initializationDate;
    }

    public void add(Dragon dragon) {
        collection.add(dragon);
        lastSavedId++;
    }

    public TreeSet<Dragon> getCollection() {
        return collection;
    }

    public ZonedDateTime getInitializationDate() {
        return initializationDate;
    }

    public String getCollectionInfo() {
        return "Collection size: " + collection.size() + "\n"
                + "Type: " + collection.getClass().getSimpleName() + "\n"
                + "Initialization date: " + initializationDate + "\n"
                + "Last saved id: " + lastSavedId;
    }

    public int getLastSavedId() {
        return lastSavedId;
    }

    public void update(int id, Dragon build) {
        collection.remove(collection.stream().filter(dragon -> dragon.id() == id).findFirst().get());
        collection.add(build);
    }
}
