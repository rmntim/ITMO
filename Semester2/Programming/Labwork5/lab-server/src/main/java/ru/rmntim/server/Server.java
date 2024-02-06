package ru.rmntim.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.rmntim.common.models.Dragon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.TreeSet;

public final class Server {
    private static TreeSet<Dragon> collection;
    private static String fileName;

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new TypeAdapter<ZonedDateTime>() {
                @Override
                public void write(JsonWriter out, ZonedDateTime value) throws IOException {
                    out.value(value.toString());
                }

                @Override
                public ZonedDateTime read(JsonReader in) throws IOException {
                    return ZonedDateTime.parse(in.nextString());
                }
            })
            .enableComplexMapKeySerialization()
            .create();

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) throws IOException {
        readFromSavedJson();
        Runtime.getRuntime().addShutdownHook(new Thread(Server::saveToJson));

        if (collection == null) {
            collection = new TreeSet<>();
        }

        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
            //@formatter:off
            for (;;) {
            //@formatter:on
                var command = reader.readLine();
                if (command == null) {
                    System.err.println("CTRL-D eblan");
                    continue;
                }
                switch (command) {
                    case "exit" -> System.exit(0);
                    default -> System.err.println("хуйню сморозил");
                }
            }
        }
    }

    private static void readFromSavedJson() throws IOException {
        fileName = System.getenv("FILENAME");
        if (fileName == null) {
            throw new IllegalArgumentException("Filename was not provided");
        }

        var inputFile = new File(fileName);
        if (!inputFile.exists()) {
            inputFile.createNewFile();
        }
        if (!inputFile.isFile()) {
            throw new IllegalArgumentException(fileName + " is a directory");
        }

        try (var inputStream = new FileInputStream(inputFile); var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            var type = new TypeToken<TreeSet<Dragon>>() {
            }.getType();
            collection = GSON.fromJson(reader, type);
        }
    }

    private static void saveToJson() {
        var outputFile = new File(fileName);
        try (var writer = new PrintWriter(outputFile)) {
            GSON.toJson(collection, writer);
        } catch (FileNotFoundException e) {
            // Should not happen, because we create the file on startup
            throw new RuntimeException(e);
        }
    }

}
