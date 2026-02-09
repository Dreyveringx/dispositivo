package com.dispositivos.catalog.infrastructure.adapter.out.persistence;

import com.dispositivos.catalog.application.ports.out.DeviceTypeRepositoryPort;
import com.dispositivos.catalog.domain.model.DeviceType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeviceTypePersistenceAdapter implements DeviceTypeRepositoryPort {

    private final DeviceTypeJpaRepository jpaRepository;

    public DeviceTypePersistenceAdapter(DeviceTypeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public DeviceType save(DeviceType deviceType) {
        DeviceTypeJpaEntity entity = toEntity(deviceType);
        DeviceTypeJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<DeviceType> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<DeviceType> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private DeviceTypeJpaEntity toEntity(DeviceType domain) {
        DeviceTypeJpaEntity e = new DeviceTypeJpaEntity();
        e.setId(domain.getId());
        e.setName(domain.getName());
        e.setDescription(domain.getDescription());
        return e;
    }

    private DeviceType toDomain(DeviceTypeJpaEntity entity) {
        return new DeviceType(entity.getId(), entity.getName(), entity.getDescription());
    }
}
