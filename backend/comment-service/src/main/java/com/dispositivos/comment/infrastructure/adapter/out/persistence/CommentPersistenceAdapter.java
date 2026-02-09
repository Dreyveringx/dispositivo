package com.dispositivos.comment.infrastructure.adapter.out.persistence;

import com.dispositivos.comment.application.ports.out.CommentRepositoryPort;
import com.dispositivos.comment.domain.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

public class CommentPersistenceAdapter implements CommentRepositoryPort {

    private final CommentJpaRepository jpaRepository;

    public CommentPersistenceAdapter(CommentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Comment save(Comment comment) {
        CommentJpaEntity entity = toEntity(comment);
        CommentJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Comment> findByDeviceId(Long deviceId) {
        return jpaRepository.findByDeviceIdOrderByCreatedAtDesc(deviceId).stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    private CommentJpaEntity toEntity(Comment domain) {
        CommentJpaEntity e = new CommentJpaEntity();
        e.setId(domain.getId());
        e.setDeviceId(domain.getDeviceId());
        e.setAuthor(domain.getAuthor());
        e.setText(domain.getText());
        e.setCreatedAt(domain.getCreatedAt());
        return e;
    }

    private Comment toDomain(CommentJpaEntity entity) {
        return new Comment(entity.getId(), entity.getDeviceId(), entity.getAuthor(), entity.getText(), entity.getCreatedAt());
    }
}
