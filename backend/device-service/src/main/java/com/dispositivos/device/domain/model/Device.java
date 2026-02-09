package com.dispositivos.device.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de dominio: Dispositivo inteligente.
 * Sin anotaciones JPA. brandId y deviceTypeId son referencias lógicas al catalog-service.
 */
public class Device {

    private Long id;
    private String name;
    private String description;
    private Long brandId;
    private Long deviceTypeId;
    private LocalDate releaseDate;
    private String imageUrl;
    private List<String> imageUrls = new ArrayList<>();

    public Device() {
    }

    /** Constructor defensivo: no acepta listas null (se usa lista vacía); copia para evitar mutación externa. */
    public Device(Long id, String name, String description, Long brandId, Long deviceTypeId,
                  LocalDate releaseDate, String imageUrl, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brandId = brandId;
        this.deviceTypeId = deviceTypeId;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
        this.imageUrls = imageUrls != null ? new ArrayList<>(imageUrls) : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }
    public Long getDeviceTypeId() { return deviceTypeId; }
    public void setDeviceTypeId(Long deviceTypeId) { this.deviceTypeId = deviceTypeId; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls != null ? new ArrayList<>(imageUrls) : new ArrayList<>();
    }
}
