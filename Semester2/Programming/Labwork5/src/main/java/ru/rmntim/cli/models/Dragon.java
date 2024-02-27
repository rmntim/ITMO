package ru.rmntim.cli.models;

import java.time.ZonedDateTime;
import java.util.Comparator;

public record Dragon(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Long age,
                     Color color, DragonType type, DragonCharacter character,
                     DragonHead head) implements Comparable<Dragon> {
    public Dragon {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid id (must be non-null and positive)");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid name (must be non-null and non-empty)");
        }
        if (coordinates == null) {
            throw new IllegalArgumentException("Invalid coordinates (must be non-null)");
        }
        if (creationDate == null) {
            throw new IllegalArgumentException("Invalid creation date (must be non-null)");
        }
        if (age == null || age <= 0) {
            throw new IllegalArgumentException("Invalid age (must be non-null and positive)");
        }
        if (color == null) {
            throw new IllegalArgumentException("Invalid color (must be non-null)");
        }
        if (type == null) {
            throw new IllegalArgumentException("Invalid type (must be non-null)");
        }
        if (character == null) {
            throw new IllegalArgumentException("Invalid character (must be non-null)");
        }
    }

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
}
