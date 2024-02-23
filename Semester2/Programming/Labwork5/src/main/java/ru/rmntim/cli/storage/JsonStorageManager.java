package ru.rmntim.cli.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.rmntim.cli.models.Dragon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.TreeSet;

public class JsonStorageManager implements StorageManager {
    private final String path;
    private final Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeJson()).enableComplexMapKeySerialization().serializeNulls().create();

    /**
     * @param path path to json file
     * @throws IllegalArgumentException if {@code path} is null or empty
     */
    public JsonStorageManager(final String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        if (path.isBlank()) {
            throw new IllegalArgumentException("Path cannot be empty");
        }
        this.path = path;
    }

    /**
     * @return collection in file, {@code null} if file is empty
     * @throws IOException                         if the file is invalid
     * @throws com.google.gson.JsonIOException     if there was a problem reading from the file
     * @throws com.google.gson.JsonSyntaxException if file contains invalid JSON
     */
    @Override
    public TreeSet<Dragon> readCollection() throws IOException {
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
            return gson.fromJson(reader, new TypeToken<TreeSet<Dragon>>() {
            }.getType());
        }
    }

    /**
     * @param collection collection to write
     * @throws IllegalArgumentException        if {@code collection} is {@code null}
     * @throws IOException                     if the file is invalid
     * @throws com.google.gson.JsonIOException if there was a problem writing to file
     */
    @Override
    public void writeCollection(final TreeSet<Dragon> collection) throws IOException {
        if (collection == null) {
            throw new IllegalArgumentException("Collection to write cannot be null");
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
            gson.toJson(collection, writer);
        }
    }
}
