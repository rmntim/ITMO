package ru.rmntim.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "user_sessions", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user-session-sequence-generator")
    @SequenceGenerator(name = "user-session-sequence-generator", sequenceName = "user_sessions_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "session_start")
    private LocalDateTime sessionStart;

    @Column(name = "session_end")
    private LocalDateTime sessionEnd;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;
}

