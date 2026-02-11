package com.dispositivos.device.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@lombok.Data
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

}
