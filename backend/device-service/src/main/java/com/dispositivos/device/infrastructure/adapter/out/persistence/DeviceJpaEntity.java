package com.dispositivos.device.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA para Device. Las imágenes adicionales se guardan como texto separado por comas
 * o en una tabla separada; para simplicidad usamos un único campo JSON-like o concatenado.
 * Usamos @ElementCollection para lista de URLs.
 */
@Entity
@Table(name = "devices")
public class DeviceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    @Column(name = "device_type_id", nullable = false)
    private Long deviceTypeId;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "device_images", joinColumns = @JoinColumn(name = "device_id"))
    @Column(name = "url", length = 500)
    private List<String> imageUrls = new ArrayList<>();

    /** Getter defensivo: nunca devuelve null para evitar NPE en mappers. */
    public List<String> getImageUrls() {
        return imageUrls != null ? imageUrls : new ArrayList<>();
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
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
}
