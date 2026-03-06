package com.lebane.departamentos.mapper;

import com.lebane.departamentos.domain.Departamento;
import com.lebane.departamentos.dto.DepartamentoRequest;
import com.lebane.departamentos.dto.DepartamentoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapeo entre Departamento (entidad) y DTOs de request/response.
 */
@Mapper(componentModel = "spring")
public interface DepartamentoMapper {

    DepartamentoResponse toResponse(Departamento entity);

    List<DepartamentoResponse> toResponseList(List<Departamento> entities);

    Departamento toEntity(DepartamentoRequest request);

    void updateEntity(DepartamentoRequest request, @MappingTarget Departamento entity);
}
