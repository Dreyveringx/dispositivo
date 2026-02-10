package com.dispositivos.catalog.infrastructure.adapter.out.persistence;

import com.dispositivos.catalog.application.ports.out.BrandRepositoryPort;
import com.dispositivos.catalog.domain.model.Brand;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BrandPersistenceAdapter implements BrandRepositoryPort {

    private final BrandJpaRepository jpaRepository;

    public BrandPersistenceAdapter(BrandJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Brand save(Brand brand) {
        BrandJpaEntity entity = toEntity(brand);
        BrandJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Brand> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private BrandJpaEntity toEntity(Brand domain) {
        BrandJpaEntity e = new BrandJpaEntity();
        e.setId(domain.getId());
        e.setName(domain.getName());
        e.setDescription(domain.getDescription());
        return e;
    }

    private Brand toDomain(BrandJpaEntity entity) {
        return new Brand(entity.getId(), entity.getName(), entity.getDescription());
    }
}
