package com.dispositivos.catalog.domain.model;

/**
 * Modelo de dominio: Marca.
 * Entidad de negocio pura, sin anotaciones JPA ni dependencias de framework.
 */
public class Brand {

    private Long id;
    private String name;
    private String description;

    public Brand() {
    }

    public Brand(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
