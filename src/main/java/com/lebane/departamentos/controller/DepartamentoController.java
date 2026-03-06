package com.lebane.departamentos.controller;

import com.lebane.departamentos.dto.DepartamentoRequest;
import com.lebane.departamentos.dto.DepartamentoResponse;
import com.lebane.departamentos.service.DepartamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST API para departamentos en venta.
 */
@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    private final DepartamentoService service;

    public DepartamentoController(DepartamentoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DepartamentoResponse>> list(
            @RequestParam(required = false) Boolean disponible,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax) {
        return ResponseEntity.ok(service.list(disponible, precioMin, precioMax));
    }

    @PostMapping
    public ResponseEntity<DepartamentoResponse> create(@RequestBody @Valid DepartamentoRequest request) {
        DepartamentoResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(created);
    }
}
