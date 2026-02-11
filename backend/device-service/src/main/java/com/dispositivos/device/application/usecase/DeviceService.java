package com.dispositivos.device.application.usecase;

import com.dispositivos.device.application.ports.in.DeviceCommand;
import com.dispositivos.device.application.ports.in.DeviceUseCases;
import com.dispositivos.device.application.ports.out.DeviceRepositoryPort;
import com.dispositivos.device.domain.exception.ResourceNotFoundException;
import com.dispositivos.device.domain.model.Device;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DeviceService implements DeviceUseCases {

    private final DeviceRepositoryPort deviceRepository;

    @Override
    public Device create(DeviceCommand command) {
        Device device = new Device(null, command.name(), command.description(), command.brandId(),
                command.deviceTypeId(), command.releaseDate(), command.imageUrl(), command.imageUrls());
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
    public Device update(Long id, DeviceCommand command) {
        Device existing = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", id));
        existing.setName(command.name());
        existing.setDescription(command.description());
        existing.setBrandId(command.brandId());
        existing.setDeviceTypeId(command.deviceTypeId());
        existing.setReleaseDate(command.releaseDate());
        existing.setImageUrl(command.imageUrl());
        existing.setImageUrls(command.imageUrls() != null ? command.imageUrls() : existing.getImageUrls());
        return deviceRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        deviceRepository.deleteById(id);
    }
}
