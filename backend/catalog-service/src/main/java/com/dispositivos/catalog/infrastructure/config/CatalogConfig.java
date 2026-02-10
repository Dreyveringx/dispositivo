package com.dispositivos.catalog.infrastructure.config;

import com.dispositivos.catalog.application.ports.in.BrandUseCases;
import com.dispositivos.catalog.application.ports.in.DeviceTypeUseCases;
import com.dispositivos.catalog.application.ports.out.BrandRepositoryPort;
import com.dispositivos.catalog.application.ports.out.DeviceTypeRepositoryPort;
import com.dispositivos.catalog.application.usecase.BrandService;
import com.dispositivos.catalog.application.usecase.DeviceTypeService;
import com.dispositivos.catalog.infrastructure.adapter.out.persistence.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfig {

    @Bean
    public BrandRepositoryPort brandRepositoryPort(BrandJpaRepository jpaRepository) {
        return new BrandPersistenceAdapter(jpaRepository);
    }

    @Bean
    public DeviceTypeRepositoryPort deviceTypeRepositoryPort(DeviceTypeJpaRepository jpaRepository) {
        return new DeviceTypePersistenceAdapter(jpaRepository);
    }

    @Bean
    public BrandUseCases brandUseCases(BrandRepositoryPort brandRepository) {
        return new BrandService(brandRepository);
    }

    @Bean
    public DeviceTypeUseCases deviceTypeUseCases(DeviceTypeRepositoryPort deviceTypeRepository) {
        return new DeviceTypeService(deviceTypeRepository);
    }
}
