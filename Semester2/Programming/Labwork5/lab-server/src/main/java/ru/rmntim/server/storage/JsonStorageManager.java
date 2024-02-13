package ru.rmntim.server.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import ru.rmntim.common.models.Dragon;
import ru.rmntim.server.ZonedDateTimeJson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonStorageManager implements StorageManager {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeJson())
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .serializeNulls()
            .create();
    private static final Logger LOGGER = Logger.getLogger(JsonStorageManager.class.getName());
    private final String fileName;

    /**
     * @param fileName Name of the json file to work with. Will be created if it doesn't exist.
     * @throws NullPointerException     If the {@code fileName} argument is {@code null}
     * @throws IllegalArgumentException If the {@code fileName} argument is empty
     */
    public JsonStorageManager(String fileName) {
        if (fileName == null) {
            throw new NullPointerException("File name is null");
        }
        if (fileName.isEmpty()) {
            throw new IllegalArgumentException("File name is empty");
        }
        this.fileName = fileName;
    }

    @Override
    public Optional<TreeSet<Dragon>> readCollection() {
        var inputFile = new File(fileName);
        try (var inputStream = new FileInputStream(inputFile); var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            if (!inputFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                inputFile.createNewFile();
            }
            if (!inputFile.isFile()) {
                LOGGER.log(Level.SEVERE, fileName + " is a directory");
                return Optional.empty();
            }
            if (!inputFile.canRead() || !inputFile.canWrite()) {
                LOGGER.log(Level.SEVERE, "Can't read/write to the file");
                return Optional.empty();
            }
            var type = new TypeToken<TreeSet<Dragon>>() {
            }.getType();

            return Optional.of(GSON.fromJson(reader, type));
        } catch (FileNotFoundException fnfe) {
            LOGGER.log(Level.SEVERE, "File is not valid: " + fnfe.getMessage());
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "Unexpected error: " + ioe.getMessage());
            System.exit(1);
        } catch (JsonParseException jpe) {
            LOGGER.log(Level.SEVERE, "Input file is corrupted");
        }

        return Optional.empty();
    }

    @Override
    public void writeCollection(final TreeSet<Dragon> collection) {
        var outputFile = new File(fileName);
        try (var writer = new PrintWriter(outputFile)) {
            GSON.toJson(collection, writer);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "Output file is not valid");
        }
    }
}
