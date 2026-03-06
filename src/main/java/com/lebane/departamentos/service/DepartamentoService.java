package com.lebane.departamentos.service;

import com.lebane.departamentos.domain.Departamento;
import com.lebane.departamentos.dto.DepartamentoRequest;
import com.lebane.departamentos.dto.DepartamentoResponse;
import com.lebane.departamentos.mapper.DepartamentoMapper;
import com.lebane.departamentos.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Lógica de negocio para departamentos.
 */
@Service
public class DepartamentoService {

    private final DepartamentoRepository repository;
    private final DepartamentoMapper mapper;

    public DepartamentoService(DepartamentoRepository repository, DepartamentoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public DepartamentoResponse create(DepartamentoRequest request) {
        Departamento entity = mapper.toEntity(request);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }
}
