package ru.rmntim.server.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.rmntim.common.models.Dragon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.TreeSet;

public class JSONStorageManager implements StorageManager {
    private final String path;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeJson())
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .create();

    /**
     * @param path path to json file
     * @throws NullPointerException     if {@code path} is null
     * @throws IllegalArgumentException if {@code path} is empty
     */
    public JSONStorageManager(final String path) {
        if (path == null) {
            throw new NullPointerException("Path cannot be null");
        }
        if (path.isBlank()) {
            throw new IllegalArgumentException("Path cannot be empty");
        }
        this.path = path;
    }

    /**
     * @return collection, initialization date and last id in file. If file is empty or collection is invalid, returns empty Optional.
     * @throws StorageException                    if the file is invalid
     * @throws com.google.gson.JsonIOException     if there was a problem reading from the file
     * @throws com.google.gson.JsonSyntaxException if file contains invalid JSON
     */
    public TreeSet<Dragon> readCollection() throws StorageException {
        var file = validateFile();
        try (var inputStream = new FileInputStream(file); var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            TreeSet<Dragon> collection = gson.fromJson(reader, new TypeToken<TreeSet<Dragon>>() {
            }.getType());

            if (collection == null) {
                return new TreeSet<>();
            }

            return collection;
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    /**
     * Checks if the file exists and if it is a valid file.
     *
     * @return valid collection file
     * @throws StorageException if the file is invalid
     */
    private File validateFile() throws StorageException {
        var file = new File(path);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new StorageException("File can't be created");
                }
            } catch (IOException e) {
                throw new StorageException(e);
            }
        }
        if (!file.isFile()) {
            throw new StorageException(path + " is not a valid file");
        }
        if (!file.canRead()) {
            throw new StorageException("File can't be read");
        }
        return file;
    }

    /**
     * @param collection collection to write
     * @throws NullPointerException            if {@code collection} is {@code null}
     * @throws StorageException                if the file is invalid
     * @throws com.google.gson.JsonIOException if there was a problem writing to file
     */
    public synchronized void writeCollection(TreeSet<Dragon> collection) throws StorageException {
        if (collection == null) {
            throw new NullPointerException();
        }
        var file = new File(path);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new StorageException("File can't be created");
                }
            } catch (IOException e) {
                throw new StorageException(e);
            }
        }
        if (!file.isFile()) {
            throw new StorageException(path + " is not a file");
        }
        if (!file.canWrite()) {
            throw new StorageException("File can't be written to");
        }
        try (var writer = new PrintWriter(file)) {
            gson.toJson(collection, writer);
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }
}
