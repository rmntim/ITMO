package ru.rmntim.cli.validators;

import ru.rmntim.cli.exceptions.ValidationException;
import ru.rmntim.cli.logic.CollectionManager;

import java.util.HashSet;

public final class CollectionValidator {
    private CollectionValidator() {
    }

    /**
     * @param collectionManager collection manager to validate
     * @throws ValidationException  if collection manager is invalid
     * @throws NullPointerException if collection manager is {@code null}
     */
    public static void validate(final CollectionManager collectionManager) throws ValidationException {
        if (collectionManager == null) {
            throw new NullPointerException("Collection manager is null");
        }
        if (collectionManager.getCollection() == null) {
            throw new ValidationException("Collection is null");
        }
        if (collectionManager.getInitializationDate() == null) {
            throw new ValidationException("Initialization date is null");
        }

        var ids = new HashSet<Integer>();
        var lastId = collectionManager.getLastSavedId();
        for (var item : collectionManager.getCollection()) {
            var id = item.id();

            if (id > lastId) {
                throw new ValidationException("Item has invalid id: " + id + " > lastSavedId (" + lastId + ")");
            }
            if (ids.contains(id)) {
                throw new ValidationException("Collection contains duplicated item with id " + id);
            } else {
                ids.add(id);
            }

            try {
                DragonValidator.validate(item);
            } catch (ValidationException e) {
                throw new ValidationException("Item with id " + id + " is invalid: " + e.getMessage());
            }
        }
    }
}
