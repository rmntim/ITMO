package ru.rmntim.common.models;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Objects;

public final class Dragon implements Comparable<Dragon>, Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private Integer id;
    private final String name;
    private final Coordinates coordinates;
    private final ZonedDateTime creationDate;
    private final Long age;
    private final Color color;
    private final DragonType type;
    private final DragonCharacter character;
    private final DragonHead head;

    public Dragon(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Long age,
                  Color color, DragonType type, DragonCharacter character,
                  DragonHead head) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.age = age;
        this.color = color;
        this.type = type;
        this.character = character;
        this.head = head;
    }

    public Dragon(String name, Coordinates coordinates, Long age, Color color, DragonType type, DragonCharacter character, DragonHead head) {
        this(null, name, coordinates, ZonedDateTime.now(), age, color, type, character, head);
    }

    public Dragon(Integer id, String name, Coordinates coordinates, Long age, Color color, DragonType type, DragonCharacter character, DragonHead head) {
        this(id, name, coordinates, ZonedDateTime.now(), age, color, type, character, head);
    }

    @Override
    public int compareTo(Dragon o) {
        return Comparator.comparing(Dragon::name)
                .thenComparing(Dragon::age)
                .thenComparing(Dragon::type)
                .thenComparing(Dragon::character)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return "name: " + name + "\n"
                + "coordinates: " + coordinates + "\n"
                + "creationDate: " + creationDate + "\n"
                + "age: " + age + "\n"
                + "color: " + color + "\n"
                + "type: " + type + "\n"
                + "character: " + character + "\n"
                + "head: " + head + "\n";
    }

    public Integer id() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public Coordinates coordinates() {
        return coordinates;
    }

    public ZonedDateTime creationDate() {
        return creationDate;
    }

    public Long age() {
        return age;
    }

    public Color color() {
        return color;
    }

    public DragonType type() {
        return type;
    }

    public DragonCharacter character() {
        return character;
    }

    public DragonHead head() {
        return head;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (Dragon) obj;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.coordinates, that.coordinates)
                && Objects.equals(this.creationDate, that.creationDate)
                && Objects.equals(this.age, that.age)
                && Objects.equals(this.color, that.color)
                && Objects.equals(this.type, that.type)
                && Objects.equals(this.character, that.character)
                && Objects.equals(this.head, that.head);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, age, color, type, character, head);
    }

}
