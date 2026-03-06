package com.lebane.departamentos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test de integración para GET /api/departamentos (TDD rojo hasta paso 11).
 * Usa perfil "test" (H2 en memoria).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DepartamentoGetIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BODY_DEPTO = """
            {
              "titulo": "Depto centro",
              "descripcion": "Cómodo",
              "precio": 120000.0,
              "moneda": "ARS",
              "metrosCuadrados": 50.0,
              "direccion": "Av. Santa Fe 500",
              "disponible": true
            }
            """;

    @Test
    @DisplayName("GET sin parámetros devuelve 200 y lista (puede ser vacía)")
    void getSinParametros_devuelve200YLista() throws Exception {
        mockMvc.perform(get("/api/departamentos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET devuelve 200 y lista con los departamentos creados")
    void get_devuelve200YListaConRecursosCreados() throws Exception {
        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_DEPTO))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/departamentos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].titulo").value("Depto centro"))
                .andExpect(jsonPath("$[0].precio").value(120000.0))
                .andExpect(jsonPath("$[0].moneda").value("ARS"))
                .andExpect(jsonPath("$[0].disponible").value(true));
    }

    @Test
    @DisplayName("GET con filtro disponible=true devuelve 200 y solo los disponibles")
    void getConFiltroDisponible_devuelve200YFiltrados() throws Exception {
        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"A","descripcion":"","precio":100.0,"moneda":"USD","metrosCuadrados":30.0,"direccion":"","disponible":true}
                                """))
                .andExpect(status().isAccepted());
        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"B","descripcion":"","precio":200.0,"moneda":"USD","metrosCuadrados":40.0,"direccion":"","disponible":false}
                                """))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/departamentos").param("disponible", "true").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("A"))
                .andExpect(jsonPath("$[0].disponible").value(true));
    }

    @Test
    @DisplayName("GET con precioMin y precioMax devuelve 200 y lista filtrada por rango")
    void getConFiltroPrecio_devuelve200YFiltrados() throws Exception {
        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Barato","descripcion":"","precio":50.0,"moneda":"USD","metrosCuadrados":25.0,"direccion":"","disponible":true}
                                """))
                .andExpect(status().isAccepted());
        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Caro","descripcion":"","precio":500.0,"moneda":"USD","metrosCuadrados":80.0,"direccion":"","disponible":true}
                                """))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/departamentos")
                        .param("precioMin", "100")
                        .param("precioMax", "600")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Caro"))
                .andExpect(jsonPath("$[0].precio").value(500.0));
    }
}
