package ru.rmntim.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "point_model", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "point-sequence-generator")
    @SequenceGenerator(name = "point-sequence-generator", sequenceName = "point_model_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Column(name = "x")
    private double x;

    @Column(name = "y")
    private double y;

    @Column(name = "r")
    private double r;

    @Column(name = "result")
    private boolean result;
}

