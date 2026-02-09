package com.dispositivos.comment.infrastructure.config;

import com.dispositivos.comment.application.ports.in.CommentUseCases;
import com.dispositivos.comment.application.ports.out.CommentRepositoryPort;
import com.dispositivos.comment.application.usecase.CommentService;
import com.dispositivos.comment.infrastructure.adapter.out.persistence.CommentJpaRepository;
import com.dispositivos.comment.infrastructure.adapter.out.persistence.CommentPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommentConfig {

    @Bean
    public CommentRepositoryPort commentRepositoryPort(CommentJpaRepository jpaRepository) {
        return new CommentPersistenceAdapter(jpaRepository);
    }

    @Bean
    public CommentUseCases commentUseCases(CommentRepositoryPort commentRepository) {
        return new CommentService(commentRepository);
    }
}
