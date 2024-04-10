package ru.rmntim.server.lib;

import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.validators.DragonValidator;
import ru.rmntim.common.validators.ValidationException;
import ru.rmntim.server.storage.StorageManager;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CollectionManager {
    private final StorageManager storageManager;
    private final TreeSet<Dragon> collection;
    private final ZonedDateTime lastInitTime = ZonedDateTime.now();
    private int lastId = 0;

    /**
     * @param storageManager storage manager to read collection from
     * @throws IOException              if collection can't be read
     * @throws ValidationException      if collection is invalid
     * @throws IllegalArgumentException if collection is null
     */
    public CollectionManager(StorageManager storageManager) throws ValidationException, IOException {
        if (storageManager == null) {
            throw new IllegalArgumentException("Storage manager cannot be null");
        }
        this.storageManager = storageManager;
        this.collection = storageManager.readCollection();
        validate();
    }

    public int getLastId() {
        return lastId;
    }

    /**
     * Validates collection and finds max id to use.
     *
     * @throws ValidationException if collection is invalid
     */
    private void validate() throws ValidationException {
        var validator = new DragonValidator();
        var id = 0;

        for (var dragon : collection) {
            validator.validate(dragon);
            id = Math.max(id, dragon.id());
        }
        lastId = id;
    }

    public TreeSet<Dragon> getCollection() {
        return collection;
    }

    /**
     * Returns info about collection.
     */
    public String getCollectionInfo() {
        return "Collection size: " + collection.size() + "\n"
                + "Initialized at: " + lastInitTime + "\n"
                + "Collection type: " + collection.getClass().getSimpleName() + "\n"
                + "Last id in collection: " + lastId;
    }

    /**
     * Saves the collection.
     * <p>
     * <strong>NOTE</strong>: If there is an error while saving, it will be ignored.
     */
    public void saveCollection() {
        try {
            storageManager.writeCollection(collection);
        } catch (IOException ignored) {
            // ignore
        }
    }

    /**
     * @return string representation of the collection
     */
    public String collectionString() {
        if (collection.isEmpty()) {
            return "Collection is empty";
        } else {
            var sj = new StringJoiner("\n");
            collection.forEach(dragon -> sj.add(dragon.toString()));
            return sj.toString();
        }
    }

    /**
     * Validates and adds dragon to the collection.
     *
     * @param dragon dragon to add
     * @throws ValidationException if dragon is invalid
     */
    public void add(Dragon dragon) throws ValidationException {
        dragon.setId(lastId + 1);
        new DragonValidator().validate(dragon);
        collection.add(dragon);
        ++lastId;
    }

    /**
     * Updates element with given id.
     *
     * @param id     id of the element to update
     * @param dragon element to insert on update
     * @return {@code false} if no elements were inserted
     * @throws ValidationException if element is invalid
     */
    public boolean update(int id, Dragon dragon) throws ValidationException {
        dragon.setId(id);
        new DragonValidator().validate(dragon);
        if (!remove(id)) {
            return false;
        }
        collection.add(dragon);
        return true;
    }

    /**
     * Remove element with given id.
     *
     * @param id id of the element to remove
     * @return {@code false} if no elements were removed
     */
    public boolean remove(int id) {
        return collection.removeIf(e -> e.id() == id);
    }

    /**
     * Clear the collection.
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Adds new element to the collection if it's greater than current maximum.
     * If collection is empty, adds element unconditionally.
     *
     * @param dragon element to add
     * @throws ValidationException if element is invalid
     */
    public void addIfMax(Dragon dragon) throws ValidationException {
        Dragon max;
        try {
            max = collection.stream().max(Dragon::compareTo).orElseThrow();
            if (dragon.compareTo(max) <= 0) {
                return;
            }
        } catch (NoSuchElementException ignored) {
            // ignore
        }
        add(dragon);
    }

    /**
     * Adds new element to the collection if it's lower than current minimum.
     * If collection is empty, adds element unconditionally.
     *
     * @param dragon element to add
     * @throws ValidationException if element is invalid
     */
    public void addIfMin(Dragon dragon) throws ValidationException {
        Dragon min;
        try {
            min = collection.stream().min(Dragon::compareTo).orElseThrow();
            if (dragon.compareTo(min) >= 0) {
                return;
            }
        } catch (NoSuchElementException ignored) {
            // ignore
        }
        add(dragon);
    }

    /**
     * Removes all elements from the collection lower that given.
     *
     * @param dragon element to compare to
     * @throws ValidationException if element is invalid
     */
    public void removeIfLower(Dragon dragon) throws ValidationException {
        dragon.setId(lastId + 1);
        new DragonValidator().validate(dragon);
        collection.removeIf(e -> e.compareTo(dragon) < 0);
    }

    /**
     * Groups elements by type and displays number of elements in each group"
     *
     * @return grouped elements as string
     */
    public String groupByType() {
        var sj = new StringJoiner("\n");
        collection.stream()
                .collect(Collectors.groupingBy(Dragon::type))
                .forEach((type, count) -> sj.add(type + ": " + count.size()));
        return sj.toString();
    }

    /**
     * Displays number of elements with character greater that given.
     *
     * @param dragon element to compare to
     * @return number of elements
     */
    public long greaterThanCharacter(Dragon dragon) throws ValidationException {
        dragon.setId(lastId + 1);
        new DragonValidator().validate(dragon);
        return collection.stream().filter(e -> e.character().compareTo(dragon.character()) > 0).count();
    }

    /**
     * Displays all elements with name starting with given string.
     *
     * @param prefix prefix to search for
     * @return elements with given prefix as string
     */
    public String startsWith(String prefix) {
        var sj = new StringJoiner("\n");
        collection.stream().filter(e -> e.name().startsWith(prefix)).forEach(e -> sj.add(e.toString()));
        return sj.toString();
    }
}
