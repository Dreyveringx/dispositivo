package com.dispositivos.device.application.ports.out;

import com.dispositivos.device.domain.model.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceRepositoryPort {

    Device save(Device device);

    List<Device> findAll();

    List<Device> findByFilters(String nameContains, Long brandId, Long deviceTypeId, boolean sortByReleaseDateDesc);

    Optional<Device> findById(Long id);

    void deleteById(Long id);
}
