package com.lebane.departamentos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test de integración para POST /api/departamentos (TDD rojo hasta implementar el endpoint en paso 9).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class DepartamentoPostIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("departamentos_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MockMvc mockMvc;

    private static final String VALID_BODY = """
            {
              "titulo": "Depto 2 ambientes",
              "descripcion": "Luminoso",
              "precio": 150000.50,
              "moneda": "USD",
              "metrosCuadrados": 45.5,
              "direccion": "Av. Corrientes 1234",
              "disponible": true
            }
            """;

    @Test
    @DisplayName("POST con body válido devuelve 202 y el recurso creado con id")
    void postConBodyValido_devuelve202YRecursoCreado() throws Exception {
        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Depto 2 ambientes"))
                .andExpect(jsonPath("$.precio").value(150000.50))
                .andExpect(jsonPath("$.moneda").value("USD"))
                .andExpect(jsonPath("$.disponible").value(true));
    }

    @Test
    @DisplayName("POST con body inválido (sin título) devuelve 400")
    void postConBodyInvalido_devuelve400() throws Exception {
        String bodySinTitulo = """
                {
                  "descripcion": "Sin título",
                  "precio": 100.0,
                  "moneda": "ARS",
                  "metrosCuadrados": 30.0,
                  "disponible": true
                }
                """;
        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodySinTitulo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isArray());
    }
}
