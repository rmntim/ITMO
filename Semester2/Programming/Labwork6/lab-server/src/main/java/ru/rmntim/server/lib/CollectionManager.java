package ru.rmntim.server.lib;

import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.validators.DragonValidator;
import ru.rmntim.common.validators.ValidationException;

import java.time.ZonedDateTime;
import java.util.TreeSet;

public class CollectionManager {
    private final TreeSet<Dragon> collection;
    private final ZonedDateTime lastInitTime = ZonedDateTime.now();
    private int lastId = 0;

    /**
     * @param collection collection to work with
     * @throws ValidationException      if collection is invalid
     * @throws IllegalArgumentException if collection is null
     */
    public CollectionManager(TreeSet<Dragon> collection) throws ValidationException {
        if (collection == null) {
            throw new IllegalArgumentException("Collection cannot be null");
        }
        this.collection = collection;
        validate();
    }

    /**
     * validates collection and finds max id to use
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

    public String getCollectionInfo() {
        return "Collection size: " + collection.size() + "\n"
                + "Initialized at: " + lastInitTime + "\n"
                + "Collection type: " + collection.getClass().getSimpleName() + "\n"
                + "Last id in collection: " + lastId;
    }
}
