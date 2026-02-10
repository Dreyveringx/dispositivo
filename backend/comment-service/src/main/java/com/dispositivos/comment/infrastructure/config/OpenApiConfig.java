package com.dispositivos.comment.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI commentOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Comment API")
                        .description("API del microservicio de comentarios por dispositivo.")
                        .version("1.0.0"));
    }
}
