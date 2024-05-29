package server.dao;

import common.domain.Color;
import common.domain.Dragon;
import common.domain.DragonCharacter;
import common.domain.DragonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity(name = "dragons")
@Table(name = "dragons", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class DragonDAO implements Serializable {
    public DragonDAO() {
    }

    public DragonDAO(Dragon dragon) {
        this.id = dragon.id();
        this.name = dragon.name();
        this.x = dragon.coordinates().x();
        this.y = dragon.coordinates().y();
        this.creationDate = dragon.creationDate();
        this.age = dragon.age();
        this.color = dragon.color();
        this.type = dragon.type();
        this.character = dragon.character();
    }

    public void update(Dragon dragon) {
        this.name = dragon.name();
        this.x = dragon.coordinates().x();
        this.y = dragon.coordinates().y();
        this.creationDate = dragon.creationDate();
        this.age = dragon.age();
        this.color = dragon.color();
        this.type = dragon.type();
        this.character = dragon.character();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, length = 11)
    private int id;

    @NotBlank(message = "Name cannot be empty")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "x", nullable = false)
    private float x;

    @Column(name = "y", nullable = false)
    private float y;

    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @Column(name = "age", nullable = false)
    private long age;

    @Column(name = "color", nullable = false)
    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DragonType type;

    @Column(name = "character", nullable = false)
    @Enumerated(EnumType.STRING)
    private DragonCharacter character;

    @OneToOne
    @JoinColumn(name = "head_id")
    private DragonHeadDAO head;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserDAO creator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotBlank(message = "Name cannot be empty") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name cannot be empty") String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public DragonType getType() {
        return type;
    }

    public void setType(DragonType type) {
        this.type = type;
    }

    public DragonCharacter getCharacter() {
        return character;
    }

    public void setCharacter(DragonCharacter character) {
        this.character = character;
    }

    public DragonHeadDAO getHead() {
        return head;
    }

    public void setHead(DragonHeadDAO head) {
        this.head = head;
    }

    public UserDAO getCreator() {
        return creator;
    }

    public void setCreator(UserDAO creator) {
        this.creator = creator;
    }
}
