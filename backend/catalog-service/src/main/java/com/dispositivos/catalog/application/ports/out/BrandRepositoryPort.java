package com.dispositivos.catalog.application.ports.out;

import com.dispositivos.catalog.domain.model.Brand;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida: persistencia de Marcas.
 * El dominio define el contrato; la infraestructura lo implementa con JPA.
 */
public interface BrandRepositoryPort {

    Brand save(Brand brand);

    List<Brand> findAll();

    Optional<Brand> findById(Long id);

    void deleteById(Long id);
}
