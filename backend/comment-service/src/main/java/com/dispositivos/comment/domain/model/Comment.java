package com.dispositivos.comment.domain.model;

import java.time.LocalDateTime;

/**
 * Modelo de dominio: Comentario asociado a un dispositivo (deviceId es referencia lógica).
 */
public class Comment {

    private Long id;
    private Long deviceId;
    private String author;
    private String text;
    private LocalDateTime createdAt;

    public Comment() {
    }

    public Comment(Long id, Long deviceId, String author, String text, LocalDateTime createdAt) {
        this.id = id;
        this.deviceId = deviceId;
        this.author = author;
        this.text = text;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
