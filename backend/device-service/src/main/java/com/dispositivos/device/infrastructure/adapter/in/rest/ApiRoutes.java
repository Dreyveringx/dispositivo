package com.dispositivos.device.infrastructure.adapter.in.rest;

/**
 * Rutas REST del API de dispositivos. Centralizadas para mantenibilidad y posible versionado.
 */
public final class ApiRoutes {

    private ApiRoutes() {
    }

    public static final String API_BASE = "/api";
    public static final String DEVICES = API_BASE + "/devices";
}
