package com.dispositivos.comment.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Entity
@Table(name = "comments")
public class CommentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private Long deviceId;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

}
