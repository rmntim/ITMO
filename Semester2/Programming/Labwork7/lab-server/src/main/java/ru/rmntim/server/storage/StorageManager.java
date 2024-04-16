package ru.rmntim.server.storage;

import ru.rmntim.common.models.Dragon;

import java.util.TreeSet;

/**
 * Interface for the managing the storage.
 */
public interface StorageManager {
    /**
     * Reads a collection of dragons from the storage.
     *
     * @return the collection of dragons read from the storage
     * @throws StorageException if there is an error reading from the storage
     */
    TreeSet<Dragon> readCollection() throws StorageException;

    /**
     * Writes the given collection of dragons to the storage.
     *
     * @param collection the collection of dragons to write
     * @throws StorageException if there is an error writing to the storage
     */
    void writeCollection(TreeSet<Dragon> collection) throws StorageException;
}
