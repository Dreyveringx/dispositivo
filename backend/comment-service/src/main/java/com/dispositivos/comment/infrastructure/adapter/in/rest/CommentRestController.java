package com.dispositivos.comment.infrastructure.adapter.in.rest;

import com.dispositivos.comment.application.ports.in.CommentUseCases;
import com.dispositivos.comment.domain.model.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST: registrar comentario y listar por deviceId.
 */
@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentRestController {

    private final CommentUseCases commentUseCases;

    public CommentRestController(CommentUseCases commentUseCases) {
        this.commentUseCases = commentUseCases;
    }

    @PostMapping
    public ResponseEntity<Comment> create(@RequestBody CommentRequest request) {
        Comment created = commentUseCases.create(
                request.getDeviceId(),
                request.getAuthor(),
                request.getText()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Comment>> listByDevice(@RequestParam Long deviceId) {
        List<Comment> list = commentUseCases.findByDeviceId(deviceId);
        return ResponseEntity.ok(list);
    }

    public static class CommentRequest {
        private Long deviceId;
        private String author;
        private String text;

        public Long getDeviceId() { return deviceId; }
        public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}
