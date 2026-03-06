package com.lebane.departamentos.controller;

import com.lebane.departamentos.dto.DepartamentoRequest;
import com.lebane.departamentos.dto.DepartamentoResponse;
import com.lebane.departamentos.dto.ErrorResponse;
import com.lebane.departamentos.service.DepartamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST API para departamentos en venta.
 */
@Tag(name = "Departamentos", description = "CRUD de departamentos en venta")
@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    private final DepartamentoService service;

    public DepartamentoController(DepartamentoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar departamentos", description = "Lista todos los departamentos con filtros opcionales por disponible, precioMin y precioMax")
    @ApiResponse(responseCode = "200", description = "Lista de departamentos (puede ser vacía)")
    @GetMapping
    public ResponseEntity<List<DepartamentoResponse>> list(
            @Parameter(description = "Filtrar por disponibilidad") @RequestParam(required = false) Boolean disponible,
            @Parameter(description = "Precio mínimo") @RequestParam(required = false) Double precioMin,
            @Parameter(description = "Precio máximo") @RequestParam(required = false) Double precioMax) {
        return ResponseEntity.ok(service.list(disponible, precioMin, precioMax));
    }

    @Operation(summary = "Actualizar departamento", description = "Actualiza un departamento por id. Body con validación Bean Validation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Departamento actualizado"),
            @ApiResponse(responseCode = "400", description = "Body inválido o error de validación", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Departamento no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> update(
            @Parameter(description = "ID del departamento") @PathVariable Long id,
            @RequestBody @Valid DepartamentoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Crear departamento", description = "Crea un nuevo departamento. Body con validación Bean Validation.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Departamento creado (recurso en body)"),
            @ApiResponse(responseCode = "400", description = "Body inválido o error de validación", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<DepartamentoResponse> create(@RequestBody @Valid DepartamentoRequest request) {
        DepartamentoResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(created);
    }
}
