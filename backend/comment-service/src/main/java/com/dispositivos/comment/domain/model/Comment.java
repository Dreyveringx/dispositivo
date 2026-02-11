package com.dispositivos.comment.domain.model;

import java.time.LocalDateTime;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class Comment {

    private Long id;
    private Long deviceId;
    private String author;
    private String text;
    private LocalDateTime createdAt;

}
