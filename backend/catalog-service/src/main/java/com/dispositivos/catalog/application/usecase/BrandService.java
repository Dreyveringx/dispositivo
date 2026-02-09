package com.dispositivos.catalog.application.usecase;

import com.dispositivos.catalog.application.ports.in.BrandUseCases;
import com.dispositivos.catalog.application.ports.out.BrandRepositoryPort;
import com.dispositivos.catalog.domain.model.Brand;

import java.util.List;
import java.util.Optional;

/**
 * Casos de uso de Marcas. Orquesta el dominio y los puertos de salida.
 */
public class BrandService implements BrandUseCases {

    private final BrandRepositoryPort brandRepository;

    public BrandService(BrandRepositoryPort brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand create(String name, String description) {
        Brand brand = new Brand(null, name, description);
        return brandRepository.save(brand);
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public Brand update(Long id, String name, String description) {
        Brand existing = brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Marca no encontrada: " + id));
        existing.setName(name);
        existing.setDescription(description);
        return brandRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }
}
