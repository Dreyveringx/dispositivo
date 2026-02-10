package com.dispositivos.catalog.application.ports.out;

import com.dispositivos.catalog.domain.model.DeviceType;

import java.util.List;
import java.util.Optional;

public interface DeviceTypeRepositoryPort {

    DeviceType save(DeviceType deviceType);

    List<DeviceType> findAll();

    Optional<DeviceType> findById(Long id);

    void deleteById(Long id);
}
