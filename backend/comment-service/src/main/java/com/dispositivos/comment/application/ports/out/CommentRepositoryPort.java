package com.dispositivos.comment.application.ports.out;

import com.dispositivos.comment.domain.model.Comment;

import java.util.List;

public interface CommentRepositoryPort {

    Comment save(Comment comment);

    List<Comment> findByDeviceId(Long deviceId);
}
