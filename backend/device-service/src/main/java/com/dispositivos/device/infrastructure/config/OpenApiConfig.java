package com.dispositivos.device.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI deviceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Device API")
                        .description("API del microservicio de dispositivos inteligentes.")
                        .version("1.0.0"));
    }
}
