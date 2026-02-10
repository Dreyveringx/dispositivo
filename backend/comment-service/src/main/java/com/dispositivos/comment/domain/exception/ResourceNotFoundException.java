package com.dispositivos.comment.domain.exception;

/**
 * Excepción de dominio cuando un recurso no existe.
 * Mapeada a HTTP 404 en la capa REST.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " no encontrado: " + id);
    }
}
