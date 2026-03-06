package com.lebane.departamentos.controller;

import com.lebane.departamentos.dto.DepartamentoRequest;
import com.lebane.departamentos.dto.DepartamentoResponse;
import com.lebane.departamentos.service.DepartamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public ResponseEntity<DepartamentoResponse> create(@RequestBody @Valid DepartamentoRequest request) {
        DepartamentoResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(created);
    }
}
