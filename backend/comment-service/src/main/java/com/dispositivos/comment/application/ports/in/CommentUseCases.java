package com.dispositivos.comment.application.ports.in;

import com.dispositivos.comment.domain.model.Comment;

import java.util.List;

public interface CommentUseCases {

    Comment create(Long deviceId, String author, String text);

    List<Comment> findByDeviceId(Long deviceId);
}
