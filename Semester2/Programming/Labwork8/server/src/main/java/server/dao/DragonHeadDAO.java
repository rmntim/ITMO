package server.dao;

import common.domain.DragonHead;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.io.Serializable;

@Entity(name = "dragon_heads")
@Table(name = "dragon_heads", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class DragonHeadDAO implements Serializable {
    public DragonHeadDAO() {
    }

    public DragonHeadDAO(DragonHead head) {
        this.eyeCount = head.eyesCount();
    }

    public void update(DragonHead head) {
        this.eyeCount = head.eyesCount();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, length = 11)
    private int id;

    @Min(value = 0, message = "Eye count cannot be negative")
    @Column(name = "eye_count", nullable = false)
    private Double eyeCount;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private UserDAO creator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @Min(value = 0, message = "Eye count cannot be negative") Double getEyeCount() {
        return eyeCount;
    }

    public void setEyeCount(@Min(value = 0, message = "Eye count cannot be negative") Double eyeCount) {
        this.eyeCount = eyeCount;
    }

    public UserDAO getCreator() {
        return creator;
    }

    public void setCreator(UserDAO creator) {
        this.creator = creator;
    }
}
