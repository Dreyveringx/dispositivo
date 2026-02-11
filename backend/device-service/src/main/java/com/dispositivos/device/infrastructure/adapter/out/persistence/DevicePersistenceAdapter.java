package com.dispositivos.device.infrastructure.adapter.out.persistence;

import com.dispositivos.device.application.ports.out.DeviceRepositoryPort;
import com.dispositivos.device.domain.model.Device;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DevicePersistenceAdapter implements DeviceRepositoryPort {

    private final DeviceJpaRepository jpaRepository;

    public DevicePersistenceAdapter(DeviceJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Device save(Device device) {
        DeviceJpaEntity entity = toEntity(device);
        DeviceJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Device> findAll() {
        List<DeviceJpaEntity> entities = jpaRepository.findAll();
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Device> findByFilters(String nameContains, Long brandId, Long deviceTypeId, boolean sortByReleaseDateDesc) {
        List<Device> list = jpaRepository.findByFilters(nameContains, brandId, deviceTypeId)
                .stream().map(this::toDomain).collect(Collectors.toList());
        if (sortByReleaseDateDesc) {
            list.sort(Comparator.comparing(Device::getReleaseDate, Comparator.nullsLast(Comparator.reverseOrder())));
        } else {
            list.sort(Comparator.comparing(Device::getReleaseDate, Comparator.nullsLast(Comparator.naturalOrder())));
        }
        return list;
    }

    @Override
    public Optional<Device> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private DeviceJpaEntity toEntity(Device domain) {
        DeviceJpaEntity e = new DeviceJpaEntity();
        e.setId(domain.getId());
        e.setName(domain.getName());
        e.setDescription(domain.getDescription());
        e.setBrandId(domain.getBrandId());
        e.setDeviceTypeId(domain.getDeviceTypeId());
        e.setReleaseDate(domain.getReleaseDate());
        e.setImageUrl(domain.getImageUrl());
        e.setImageUrls(nullSafeList(domain.getImageUrls()));
        return e;
    }

    private Device toDomain(DeviceJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        List<String> safeUrls = copyOrEmpty(entity.getImageUrls());
        return new Device(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getBrandId(),
                entity.getDeviceTypeId(),
                entity.getReleaseDate(),
                entity.getImageUrl(),
                safeUrls
        );
    }

    private static List<String> nullSafeList(List<String> list) {
        return list != null ? list : new ArrayList<>();
    }

    private static List<String> copyOrEmpty(List<String> list) {
        return list != null ? new ArrayList<>(list) : new ArrayList<>();
    }
}
