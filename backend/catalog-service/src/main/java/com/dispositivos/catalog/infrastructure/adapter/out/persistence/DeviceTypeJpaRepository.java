package com.dispositivos.catalog.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceTypeJpaRepository extends JpaRepository<DeviceTypeJpaEntity, Long> {
}
