package com.dispositivos.device.application.ports.in;

import com.dispositivos.device.domain.model.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceUseCases {

    Device create(DeviceCommand command);

    List<Device> findAll();

    List<Device> findFiltered(String nameContains, Long brandId, Long deviceTypeId, boolean sortByReleaseDateDesc);

    Optional<Device> findById(Long id);

    Device update(Long id, DeviceCommand command);

    void deleteById(Long id);
}
