package ru.rmntim.server.lib;

import ru.rmntim.common.exceptions.ValidationException;
import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.validators.DragonValidator;

import java.time.ZonedDateTime;
import java.util.TreeSet;

public class CollectionManager {
    private final TreeSet<Dragon> collection;
    private final ZonedDateTime lastInitTime;

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
        this.lastInitTime = ZonedDateTime.now();
        validate();
    }

    /**
     * validates collection
     *
     * @throws ValidationException if collection is invalid
     */
    private void validate() throws ValidationException {
        var validator = new DragonValidator();
        for (var dragon : collection) {
            validator.validate(dragon);
        }
    }
}
