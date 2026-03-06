package com.lebane.departamentos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI (Swagger) para la documentación de la API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Departamentos en Venta")
                        .version("1.0.0")
                        .description("API REST para gestión de departamentos en venta. Endpoints: listado con filtros (GET), creación (POST), actualización (PUT)."));
    }
}
