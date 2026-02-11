package com.dispositivos.comment.infrastructure.adapter.in.rest;

import com.dispositivos.comment.application.ports.in.CommentUseCases;
import com.dispositivos.comment.infrastructure.adapter.in.rest.dto.CommentRequest;
import com.dispositivos.comment.infrastructure.adapter.in.rest.dto.CommentResponse;
import com.dispositivos.comment.infrastructure.adapter.in.rest.mapper.CommentMapper;
import com.dispositivos.comment.infrastructure.config.ApiDoc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiRoutes.COMMENTS)
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Comentarios por dispositivo")
public class CommentRestController {

    private final CommentUseCases commentUseCases;

    @PostMapping
    @Operation(summary = "Crear comentario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comentario creado"),
            @ApiResponse(responseCode = "400", description = ApiDoc.ERROR_400),
            @ApiResponse(responseCode = "404", description = ApiDoc.ERROR_404_GENERIC)
    })
    public ResponseEntity<CommentResponse> create(@Valid @RequestBody CommentRequest request) {
        var created = commentUseCases.create(
                request.getDeviceId(),
                request.getAuthor(),
                request.getText()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(CommentMapper.toResponse(created));
    }

    @GetMapping
    @Operation(summary = "Listar comentarios por dispositivo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de comentarios"),
            @ApiResponse(responseCode = "400", description = ApiDoc.ERROR_400_PARAMS)
    })
    public ResponseEntity<List<CommentResponse>> listByDevice(
            @Parameter(description = "ID del dispositivo") @RequestParam Long deviceId) {
        return ResponseEntity.ok(
                CommentMapper.toResponseList(commentUseCases.findByDeviceId(deviceId))
        );
    }
}
