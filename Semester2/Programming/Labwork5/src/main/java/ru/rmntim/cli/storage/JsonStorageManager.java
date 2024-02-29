package ru.rmntim.cli.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.rmntim.cli.exceptions.ValidationException;
import ru.rmntim.cli.logic.CollectionManager;
import ru.rmntim.cli.validators.CollectionValidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.Optional;

public class JsonStorageManager {
    private final String path;
    private final Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeJson()).enableComplexMapKeySerialization().serializeNulls().create();

    /**
     * @param path path to json file
     * @throws NullPointerException     if path is {@code null}
     * @throws IllegalArgumentException if {@code path} is empty
     */
    public JsonStorageManager(final String path) {
        if (path == null) {
            throw new NullPointerException();
        }
        if (path.isBlank()) {
            throw new IllegalArgumentException("Path cannot be empty");
        }
        this.path = path;
    }

    /**
     * @return collection, initialization date and last id in file. If file is empty or collection is invalid, returns empty Optional.
     * @throws IOException                         if the file is invalid
     * @throws ValidationException                 if collection is invalid
     * @throws com.google.gson.JsonIOException     if there was a problem reading from the file
     * @throws com.google.gson.JsonSyntaxException if file contains invalid JSON
     */
    public Optional<CollectionManager> readCollection() throws IOException, ValidationException {
        var file = new File(path);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("File can't be created");
            }
        }
        if (!file.isFile()) {
            throw new IOException(path + " is not a valid file");
        }
        if (!file.canRead()) {
            throw new IOException("File can't be read");
        }
        try (var inputStream = new FileInputStream(file); var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            CollectionManager collectionManager = gson.fromJson(reader, new TypeToken<CollectionManager>() {
            }.getType());

            if (collectionManager == null) {
                return Optional.empty();
            } else {
                new CollectionValidator().validate(collectionManager);
            }

            return Optional.of(collectionManager);
        }
    }

    /**
     * @param collectionManager collection to write
     * @throws NullPointerException            if {@code collection} is {@code null}
     * @throws IOException                     if the file is invalid
     * @throws com.google.gson.JsonIOException if there was a problem writing to file
     */
    public void writeCollection(final CollectionManager collectionManager) throws IOException {
        if (collectionManager == null) {
            throw new NullPointerException();
        }
        var file = new File(path);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("File can't be created");
            }
        }
        if (!file.isFile()) {
            throw new IOException(path + " is not a file");
        }
        if (!file.canWrite()) {
            throw new IOException("File can't be written to");
        }
        try (var writer = new PrintWriter(file)) {
            gson.toJson(collectionManager, writer);
        }
    }
}
