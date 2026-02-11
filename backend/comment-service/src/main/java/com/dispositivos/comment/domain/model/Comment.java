package com.dispositivos.comment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;
    private Long deviceId;
    private String author;
    private String text;
    private LocalDateTime createdAt;

}
