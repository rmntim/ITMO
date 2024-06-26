package ru.rmntim.cli.models;

import java.time.ZonedDateTime;
import java.util.Comparator;

public record Dragon(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Long age,
                     Color color, DragonType type, DragonCharacter character,
                     DragonHead head) implements Comparable<Dragon> {
    public Dragon(Integer id, String name, Coordinates coordinates, Long age, Color color, DragonType type, DragonCharacter character, DragonHead head) {
        this(id, name, coordinates, ZonedDateTime.now(), age, color, type, character, head);
    }

    @Override
    public int compareTo(Dragon o) {
        return Comparator.comparing(Dragon::type)
                .thenComparing(Dragon::character)
                .thenComparing(Dragon::age)
                .thenComparing(Dragon::name)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return "id: " + id + "\n"
                + "name: " + name + "\n"
                + "coordinates: " + coordinates + "\n"
                + "creationDate: " + creationDate + "\n"
                + "age: " + age + "\n"
                + "color: " + color + "\n"
                + "type: " + type + "\n"
                + "character: " + character + "\n"
                + "head: " + head + "\n";
    }
}
