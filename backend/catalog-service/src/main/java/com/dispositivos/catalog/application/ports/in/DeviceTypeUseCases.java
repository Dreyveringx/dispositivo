package com.dispositivos.catalog.application.ports.in;

import com.dispositivos.catalog.domain.model.DeviceType;

import java.util.List;
import java.util.Optional;

public interface DeviceTypeUseCases {

    DeviceType create(String name, String description);

    List<DeviceType> findAll();

    Optional<DeviceType> findById(Long id);

    DeviceType update(Long id, String name, String description);

    void deleteById(Long id);
}
