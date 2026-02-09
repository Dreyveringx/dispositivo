package com.dispositivos.comment.application.usecase;

import com.dispositivos.comment.application.ports.in.CommentUseCases;
import com.dispositivos.comment.application.ports.out.CommentRepositoryPort;
import com.dispositivos.comment.domain.model.Comment;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommentService implements CommentUseCases {

    private final CommentRepositoryPort commentRepository;

    public CommentService(CommentRepositoryPort commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment create(Long deviceId, String author, String text) {
        Comment comment = new Comment(null, deviceId, author, text, LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findByDeviceId(Long deviceId) {
        return commentRepository.findByDeviceId(deviceId).stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }
}
