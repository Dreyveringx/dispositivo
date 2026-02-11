package com.dispositivos.device.application.ports.in;

import com.dispositivos.device.domain.model.Device;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DeviceUseCases {

    Device create(String name, String description, Long brandId, Long deviceTypeId,
                  LocalDate releaseDate, String imageUrl, List<String> imageUrls);

    List<Device> findAll();

    List<Device> findFiltered(String nameContains, Long brandId, Long deviceTypeId, boolean sortByReleaseDateDesc);

    Optional<Device> findById(Long id);

    Device update(Long id, String name, String description, Long brandId, Long deviceTypeId,
                  LocalDate releaseDate, String imageUrl, List<String> imageUrls);

    void deleteById(Long id);
}
