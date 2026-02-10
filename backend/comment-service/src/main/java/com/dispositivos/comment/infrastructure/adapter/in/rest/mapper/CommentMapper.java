package com.dispositivos.comment.infrastructure.adapter.in.rest.mapper;

import com.dispositivos.comment.domain.model.Comment;
import com.dispositivos.comment.infrastructure.adapter.in.rest.dto.CommentResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static CommentResponse toResponse(Comment domain) {
        if (domain == null) {
            return null;
        }
        CommentResponse dto = new CommentResponse();
        dto.setId(domain.getId());
        dto.setDeviceId(domain.getDeviceId());
        dto.setAuthor(domain.getAuthor());
        dto.setText(domain.getText());
        dto.setCreatedAt(domain.getCreatedAt());
        return dto;
    }

    public static List<CommentResponse> toResponseList(List<Comment> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(CommentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
