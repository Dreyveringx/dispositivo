package com.dispositivos.device.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceJpaRepository extends JpaRepository<DeviceJpaEntity, Long> {

    @Query("SELECT d FROM DeviceJpaEntity d WHERE " +
            "(:name is null or :name = '' or lower(d.name) like lower(concat('%', :name, '%'))) AND " +
            "(:brandId is null or d.brandId = :brandId) AND " +
            "(:deviceTypeId is null or d.deviceTypeId = :deviceTypeId)")
    List<DeviceJpaEntity> findByFilters(
            @Param("name") String nameContains,
            @Param("brandId") Long brandId,
            @Param("deviceTypeId") Long deviceTypeId
    );
}
