package com.lebane.departamentos.service;

import com.lebane.departamentos.domain.Departamento;
import com.lebane.departamentos.domain.Moneda;
import com.lebane.departamentos.dto.DepartamentoRequest;
import com.lebane.departamentos.dto.DepartamentoResponse;
import com.lebane.departamentos.exception.DepartamentoNotFoundException;
import com.lebane.departamentos.mapper.DepartamentoMapper;
import com.lebane.departamentos.repository.DepartamentoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios de DepartamentoService.
 */
@ExtendWith(MockitoExtension.class)
class DepartamentoServiceTest {

    @Mock
    private DepartamentoRepository repository;

    @Mock
    private DepartamentoMapper mapper;

    @InjectMocks
    private DepartamentoService service;

    @Test
    @DisplayName("create: mapea request a entidad, guarda y devuelve response")
    void create_mapeaGuardaYDevuelveResponse() {
        DepartamentoRequest request = requestValido();
        Departamento entity = new Departamento();
        Departamento entityGuardada = new Departamento();
        entityGuardada.setId(1L);
        DepartamentoResponse response = new DepartamentoResponse();
        response.setId(1L);
        response.setTitulo("Depto test");

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entityGuardada);
        when(mapper.toResponse(entityGuardada)).thenReturn(response);

        DepartamentoResponse result = service.create(request);

        assertThat(result).isSameAs(response);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitulo()).isEqualTo("Depto test");
        verify(mapper).toEntity(request);
        verify(repository).save(entity);
        verify(mapper).toResponse(entityGuardada);
    }

    @Test
    @DisplayName("list: sin filtros devuelve lista mapeada")
    void list_sinFiltros_devuelveListaMapeada() {
        List<Departamento> entities = List.of(new Departamento());
        List<DepartamentoResponse> responses = List.of(new DepartamentoResponse());

        when(repository.findAll(any(Specification.class))).thenReturn(entities);
        when(mapper.toResponse(entities.get(0))).thenReturn(responses.get(0));

        List<DepartamentoResponse> result = service.list(null, null, null);

        assertThat(result).hasSize(1).isEqualTo(responses);
        verify(repository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("list: con filtros devuelve lista filtrada")
    void list_conFiltros_devuelveListaFiltrada() {
        when(repository.findAll(any(Specification.class))).thenReturn(List.of());

        List<DepartamentoResponse> result = service.list(true, 100.0, 500.0);

        assertThat(result).isEmpty();
        verify(repository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("update: con id existente actualiza y devuelve response")
    void update_idExistente_actualizaYDevuelveResponse() {
        Long id = 1L;
        DepartamentoRequest request = requestValido();
        Departamento entity = new Departamento();
        entity.setId(id);
        DepartamentoResponse response = new DepartamentoResponse();
        response.setId(id);
        response.setTitulo("Actualizado");

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        DepartamentoResponse result = service.update(id, request);

        assertThat(result).isSameAs(response);
        assertThat(result.getTitulo()).isEqualTo("Actualizado");
        verify(repository).findById(id);
        verify(mapper).updateEntity(request, entity);
        verify(repository).save(entity);
        verify(mapper).toResponse(entity);
    }

    @Test
    @DisplayName("update: con id inexistente lanza DepartamentoNotFoundException")
    void update_idInexistente_lanzaDepartamentoNotFoundException() {
        Long id = 99999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(id, requestValido()))
                .isInstanceOf(DepartamentoNotFoundException.class)
                .hasMessageContaining("99999");

        verify(repository).findById(id);
        verify(mapper, never()).updateEntity(any(), any());
        verify(repository, never()).save(any());
    }

    private static DepartamentoRequest requestValido() {
        DepartamentoRequest r = new DepartamentoRequest();
        r.setTitulo("Depto test");
        r.setDescripcion("Desc");
        r.setPrecio(100000.0);
        r.setMoneda(Moneda.ARS);
        r.setMetrosCuadrados(50.0);
        r.setDireccion("Calle 123");
        r.setDisponible(true);
        return r;
    }
}
