package ru.rmntim.cli.models;

import java.time.ZonedDateTime;
import java.util.Comparator;

public record Dragon(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Long age,
                     Color color, DragonType type, DragonCharacter character,
                     DragonHead head) implements Comparable<Dragon> {
    @Override
    public int compareTo(Dragon o) {
        return Comparator.comparing(Dragon::type)
                .thenComparing(Dragon::character)
                .thenComparing(Dragon::age)
                .thenComparing(Dragon::name)
                .compare(this, o);
    }
}
