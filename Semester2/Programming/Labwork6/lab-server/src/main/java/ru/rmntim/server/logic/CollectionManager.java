package ru.rmntim.server.logic;

import ru.rmntim.server.models.Dragon;

import java.time.ZonedDateTime;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * Class that encapsulates logic of dragon collection
 * and provides methods for controlling it
 */
public class CollectionManager {
    private final TreeSet<Dragon> collection;
    private final ZonedDateTime initializationDate;
    private int lastSavedId;

    public CollectionManager(final TreeSet<Dragon> collection, ZonedDateTime initializationDate, int lastSavedId) {
        this.collection = collection;
        this.lastSavedId = lastSavedId;
        this.initializationDate = initializationDate;
    }

    /**
     * Add new dragon to collection and increment lastSavedId
     *
     * @param dragon dragon to add
     */
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

    /**
     * @param id             id of dragon to update
     * @param dragonSupplier function that supplies new dragon. Not passing the dragon because we need to check ids before parsing it
     * @throws IllegalArgumentException if id is out of range
     */
    public void update(int id, Supplier<Dragon> dragonSupplier) {
        if (containsId(id)) {
            remove(id);
            collection.add(dragonSupplier.get());
        } else {
            throw new IllegalArgumentException("Dragon with id " + id + " not found");
        }
    }

    /**
     * @param id id of dragon to remove
     * @throws IllegalArgumentException if id is out of range
     */
    public void remove(int id) {
        if (containsId(id)) {
            collection.remove(collection.stream().filter(dragon -> dragon.id() == id).findFirst().orElseThrow());
        } else {
            throw new IllegalArgumentException("Dragon with id " + id + " not found");
        }
    }

    /**
     * @param id id of dragon to check for
     * @return {@code true} if dragon with id is in collection
     */
    public boolean containsId(int id) {
        return collection.stream().anyMatch(dragon -> dragon.id() == id);
    }

    /**
     * Clears collection and sets {@code lastSavedId} to 0
     */
    public void clear() {
        collection.clear();
        lastSavedId = 0;
    }
}
