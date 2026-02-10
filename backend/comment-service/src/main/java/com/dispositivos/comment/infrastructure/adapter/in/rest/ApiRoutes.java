package com.dispositivos.comment.infrastructure.adapter.in.rest;

/**
 * Rutas REST del API de comentarios. Centralizadas para mantenibilidad y posible versionado.
 */
public final class ApiRoutes {

    private ApiRoutes() {
    }

    public static final String API_BASE = "/api";
    public static final String COMMENTS = API_BASE + "/comments";
}
