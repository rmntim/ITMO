package ru.rmntim.server;

import com.google.gson.GsonBuilder;
import ru.rmntim.common.util.ZonedDateTimeJson;
import ru.rmntim.server.storage.JsonStorageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.TreeSet;

public final class Server {
    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) throws IOException {
        var dumper = new JsonStorageManager(System.getenv("FILENAME"));
        var collection = dumper.readCollection().orElse(new TreeSet<>());

        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {
            //@formatter:off
            for (;;) {
            //@formatter:on
                var command = reader.readLine();
                if (command == null) {
                    System.exit(0);
                }

                var gson = new GsonBuilder().serializeNulls().setPrettyPrinting()
                        .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeJson())
                        .enableComplexMapKeySerialization()
                        .create();

                switch (command) {
                    case "exit" -> System.exit(0);
                    case "save" -> dumper.writeCollection(collection);
                    case "show" -> System.out.println(gson.toJson(collection));
                    default -> System.err.println("хуйню сморозил");
                }
            }
        }
    }
}
