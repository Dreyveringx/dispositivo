package com.dispositivos.catalog.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Entity
@Table(name = "brands")
public class BrandJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

}
