package com.dispositivos.catalog.application.usecase;

import com.dispositivos.catalog.application.ports.in.DeviceTypeUseCases;
import com.dispositivos.catalog.application.ports.out.DeviceTypeRepositoryPort;
import com.dispositivos.catalog.domain.exception.ResourceNotFoundException;
import com.dispositivos.catalog.domain.model.DeviceType;

import java.util.List;
import java.util.Optional;

public class DeviceTypeService implements DeviceTypeUseCases {

    private final DeviceTypeRepositoryPort deviceTypeRepository;

    public DeviceTypeService(DeviceTypeRepositoryPort deviceTypeRepository) {
        this.deviceTypeRepository = deviceTypeRepository;
    }

    @Override
    public DeviceType create(String name, String description) {
        DeviceType deviceType = new DeviceType(null, name, description);
        return deviceTypeRepository.save(deviceType);
    }

    @Override
    public List<DeviceType> findAll() {
        return deviceTypeRepository.findAll();
    }

    @Override
    public Optional<DeviceType> findById(Long id) {
        return deviceTypeRepository.findById(id);
    }

    @Override
    public DeviceType update(Long id, String name, String description) {
        DeviceType existing = deviceTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de dispositivo", id));
        existing.setName(name);
        existing.setDescription(description);
        return deviceTypeRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        deviceTypeRepository.deleteById(id);
    }
}
