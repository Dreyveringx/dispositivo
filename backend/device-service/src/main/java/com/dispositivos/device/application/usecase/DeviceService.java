package com.dispositivos.device.application.usecase;

import com.dispositivos.device.application.ports.in.DeviceUseCases;
import com.dispositivos.device.application.ports.out.DeviceRepositoryPort;
import com.dispositivos.device.domain.exception.ResourceNotFoundException;
import com.dispositivos.device.domain.model.Device;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DeviceService implements DeviceUseCases {

    private final DeviceRepositoryPort deviceRepository;

    public DeviceService(DeviceRepositoryPort deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device create(String name, String description, Long brandId, Long deviceTypeId,
                         LocalDate releaseDate, String imageUrl, List<String> imageUrls) {
        Device device = new Device(null, name, description, brandId, deviceTypeId,
                releaseDate, imageUrl, imageUrls);
        return deviceRepository.save(device);
    }

    @Override
    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    @Override
    public List<Device> findFiltered(String nameContains, Long brandId, Long deviceTypeId, boolean sortByReleaseDateDesc) {
        return deviceRepository.findByFilters(nameContains, brandId, deviceTypeId, sortByReleaseDateDesc);
    }

    @Override
    public Optional<Device> findById(Long id) {
        return deviceRepository.findById(id);
    }

    @Override
    public Device update(Long id, String name, String description, Long brandId, Long deviceTypeId,
                        LocalDate releaseDate, String imageUrl, List<String> imageUrls) {
        Device existing = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", id));
        existing.setName(name);
        existing.setDescription(description);
        existing.setBrandId(brandId);
        existing.setDeviceTypeId(deviceTypeId);
        existing.setReleaseDate(releaseDate);
        existing.setImageUrl(imageUrl);
        existing.setImageUrls(imageUrls != null ? imageUrls : existing.getImageUrls());
        return deviceRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        deviceRepository.deleteById(id);
    }
}
