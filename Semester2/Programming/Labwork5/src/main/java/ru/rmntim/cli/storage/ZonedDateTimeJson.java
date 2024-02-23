package ru.rmntim.cli.storage;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.ZonedDateTime;

public class ZonedDateTimeJson extends TypeAdapter<ZonedDateTime> {
    @Override
    public void write(JsonWriter out, ZonedDateTime value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public ZonedDateTime read(JsonReader in) throws IOException {
        return ZonedDateTime.parse(in.nextString());
    }
}
