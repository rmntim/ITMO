package ru.rmntim.server.storage;

import ru.rmntim.common.models.Dragon;

import java.io.IOException;
import java.util.TreeSet;

public interface StorageManager {
    /**
     * @return collection in file, {@code null} if file is empty
     * @throws IOException if the file is invalid
     */
    TreeSet<Dragon> readCollection() throws IOException;

    /**
     * @param collection collection to write
     * @throws IOException if the file is invalid
     */
    void writeCollection(TreeSet<Dragon> collection) throws IOException;
}
