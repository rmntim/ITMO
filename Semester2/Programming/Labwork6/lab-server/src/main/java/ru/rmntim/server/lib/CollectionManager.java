package ru.rmntim.server.lib;

import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.validators.DragonValidator;
import ru.rmntim.common.validators.ValidationException;
import ru.rmntim.server.storage.StorageManager;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.TreeSet;

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
        }
    }
}
