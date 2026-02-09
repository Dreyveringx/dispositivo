package com.dispositivos.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del microservicio de catálogo.
 * Solo arranca Spring; la configuración de beans hexagonales está en infrastructure.config.
 */
@SpringBootApplication(scanBasePackages = "com.dispositivos.catalog")
public class CatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogServiceApplication.class, args);
    }
}
