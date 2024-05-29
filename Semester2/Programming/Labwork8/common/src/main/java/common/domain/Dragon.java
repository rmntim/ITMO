package common.domain;

import common.user.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public final class Dragon implements Comparable<Dragon>, Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private Integer id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationDate;
    private Long age;
    private Color color;
    private DragonType type;
    private DragonCharacter character;
    private DragonHead head;
    private final User creator;

    public Dragon(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Long age,
                  Color color, DragonType type, DragonCharacter character,
                  DragonHead head, User creator) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.age = age;
        this.color = color;
        this.type = type;
        this.character = character;
        this.head = head;
        this.creator = creator;
    }

    @Override
    public int compareTo(Dragon o) {
        return this.id - o.id();
    }

    public void update(Dragon d) {
        this.id = d.id();
        this.name = d.name();
        this.coordinates = d.coordinates();
        this.creationDate = d.creationDate();
        this.age = d.age();
        this.color = d.color();
        this.type = d.type();
        this.character = d.character();
        this.head = d.head();
    }

    public Dragon copy(int id, User creator) {
        return new Dragon(
                id, this.name, this.coordinates, this.creationDate, this.age,
                this.color, this.type, this.character, this.head, creator
        );
    }

    public boolean validate() {
        if (id == null || id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (creationDate == null) return false;
        if (age == null || age <= 0) return false;
        if (color == null) return false;
        if (type == null) return false;
        return character != null;
    }

    public Integer id() {
        return id;
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

    public User creator() {
        return creator;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Dragon) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.coordinates, that.coordinates) &&
                Objects.equals(this.creationDate, that.creationDate) &&
                Objects.equals(this.age, that.age) &&
                Objects.equals(this.color, that.color) &&
                Objects.equals(this.type, that.type) &&
                Objects.equals(this.character, that.character) &&
                Objects.equals(this.head, that.head) &&
                Objects.equals(this.creator, that.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, age, color, type, character, head, creator);
    }

    @Override
    public String toString() {
        return "Dragon[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "coordinates=" + coordinates + ", " +
                "creationDate=" + creationDate + ", " +
                "age=" + age + ", " +
                "color=" + color + ", " +
                "type=" + type + ", " +
                "character=" + character + ", " +
                "head=" + head + ", " +
                "creator=" + creator + ']';
    }

}
