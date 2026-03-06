package com.lebane.departamentos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

/**
 * Configuración de OpenAPI (Swagger) para la documentación de la API.
 */
@Configuration
public class OpenApiConfig {

    private static final Logger log = LoggerFactory.getLogger(OpenApiConfig.class);

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Departamentos en Venta")
                        .version("1.0.0")
                        .description("API REST para gestión de departamentos en venta. Endpoints: listado con filtros (GET), creación (POST), actualización (PUT)."));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logSwaggerUrl(ApplicationReadyEvent event) {
        Environment env = event.getApplicationContext().getEnvironment();
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String baseUrl = "http://localhost:" + port + contextPath;
        String swaggerUrl = baseUrl + (baseUrl.endsWith("/") ? "" : "/") + "swagger-ui.html";
        log.info("------------------------------------------------------------------------");
        log.info("  Swagger UI disponible en: {}", swaggerUrl);
        log.info("------------------------------------------------------------------------");
    }
}
