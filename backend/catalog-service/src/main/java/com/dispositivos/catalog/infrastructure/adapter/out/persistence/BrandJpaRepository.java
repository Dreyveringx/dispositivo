package com.dispositivos.catalog.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data. Solo se usa desde el adaptador que implementa el puerto.
 */
public interface BrandJpaRepository extends JpaRepository<BrandJpaEntity, Long> {
}
