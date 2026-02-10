package com.dispositivos.catalog.application.ports.in;

import com.dispositivos.catalog.domain.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandUseCases {

    Brand create(String name, String description);

    List<Brand> findAll();

    Optional<Brand> findById(Long id);

    Brand update(Long id, String name, String description);

    void deleteById(Long id);
}
