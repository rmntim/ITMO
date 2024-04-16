package ru.rmntim.server.storage;

/**
 * Represents errors while working with the storage.
 */
public class StorageException extends Exception {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }
}
