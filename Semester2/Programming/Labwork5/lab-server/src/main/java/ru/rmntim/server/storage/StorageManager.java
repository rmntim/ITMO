package ru.rmntim.server.storage;

import ru.rmntim.common.models.Dragon;

import java.util.Optional;
import java.util.TreeSet;

/**
 * Interface for saving and reading the collection to/from any desired storage.
 */
public interface StorageManager {
    /**
     * Reads collection from the storage
     *
     * @return Collection if input is valid, empty Optional otherwise
     */
    Optional<TreeSet<Dragon>> readCollection();

    /**
     * Saves collection to the storage
     *
     * @param collection Collection to be saved
     */
    void writeCollection(TreeSet<Dragon> collection);
}
