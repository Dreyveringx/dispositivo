package com.dispositivos.device.infrastructure.config;

import com.dispositivos.device.application.ports.in.DeviceUseCases;
import com.dispositivos.device.application.ports.out.DeviceRepositoryPort;
import com.dispositivos.device.application.usecase.DeviceService;
import com.dispositivos.device.infrastructure.adapter.out.persistence.DeviceJpaRepository;
import com.dispositivos.device.infrastructure.adapter.out.persistence.DevicePersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceConfig {

    @Bean
    public DeviceRepositoryPort deviceRepositoryPort(DeviceJpaRepository jpaRepository) {
        return new DevicePersistenceAdapter(jpaRepository);
    }

    @Bean
    public DeviceUseCases deviceUseCases(DeviceRepositoryPort deviceRepository) {
        return new DeviceService(deviceRepository);
    }
}
