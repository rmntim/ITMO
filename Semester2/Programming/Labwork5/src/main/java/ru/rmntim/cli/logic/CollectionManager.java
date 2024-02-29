package ru.rmntim.cli.logic;

import ru.rmntim.cli.models.Dragon;

import java.time.ZonedDateTime;
import java.util.TreeSet;
import java.util.function.Supplier;

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

    public void update(int id, Supplier<Dragon> dragonSupplier) {
        if (containsId(id)) {
            remove(id);
            collection.add(dragonSupplier.get());
        } else {
            throw new IllegalArgumentException("Dragon with id " + id + " not found");
        }
    }

    public void remove(int id) {
        if (containsId(id)) {
            collection.remove(collection.stream().filter(dragon -> dragon.id() == id).findFirst().orElseThrow());
        } else {
            throw new IllegalArgumentException("Dragon with id " + id + " not found");
        }
    }

    public boolean containsId(int id) {
        return collection.stream().anyMatch(dragon -> dragon.id() == id);
    }

    public void clear() {
        collection.clear();
        lastSavedId = 0;
    }
}
