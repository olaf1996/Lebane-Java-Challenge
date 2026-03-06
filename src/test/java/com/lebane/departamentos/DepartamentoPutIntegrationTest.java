package com.lebane.departamentos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.jayway.jsonpath.JsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test de integración para PUT /api/departamentos/{id} (TDD rojo hasta paso 13).
 * Usa perfil "test" (H2 en memoria).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DepartamentoPutIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BODY_VALIDO = """
            {
              "titulo": "Depto actualizado",
              "descripcion": "Renovado",
              "precio": 180000.0,
              "moneda": "USD",
              "metrosCuadrados": 55.0,
              "direccion": "Av. Libertad 200",
              "disponible": false
            }
            """;

    @Test
    @DisplayName("PUT con id existente y body válido devuelve 200 y el recurso actualizado")
    void putConIdExistenteYBodyValido_devuelve200YRecursoActualizado() throws Exception {
        Long id = crearDepartamentoYObtenerId();

        mockMvc.perform(put("/api/departamentos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_VALIDO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.titulo").value("Depto actualizado"))
                .andExpect(jsonPath("$.precio").value(180000.0))
                .andExpect(jsonPath("$.moneda").value("USD"))
                .andExpect(jsonPath("$.disponible").value(false));
    }

    @Test
    @DisplayName("PUT con id inexistente devuelve 404")
    void putConIdInexistente_devuelve404() throws Exception {
        mockMvc.perform(put("/api/departamentos/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_VALIDO))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("PUT con body inválido (sin título) devuelve 400")
    void putConBodyInvalido_devuelve400() throws Exception {
        Long id = crearDepartamentoYObtenerId();
        String bodySinTitulo = """
                {
                  "descripcion": "Sin título",
                  "precio": 100.0,
                  "moneda": "ARS",
                  "metrosCuadrados": 30.0,
                  "disponible": true
                }
                """;

        mockMvc.perform(put("/api/departamentos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodySinTitulo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isArray());
    }

    private Long crearDepartamentoYObtenerId() throws Exception {
        String body = """
                {
                  "titulo": "Depto original",
                  "descripcion": "Para actualizar",
                  "precio": 100000.0,
                  "moneda": "ARS",
                  "metrosCuadrados": 40.0,
                  "direccion": "Calle Falsa 123",
                  "disponible": true
                }
                """;
        ResultActions result = mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").exists());
        String response = result.andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.parse(response).read("$.id")).longValue();
    }
}
