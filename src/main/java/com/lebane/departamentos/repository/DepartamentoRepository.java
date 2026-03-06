package com.lebane.departamentos.repository;

import com.lebane.departamentos.domain.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repositorio de acceso a datos para Departamentos.
 * Extiende JpaRepository (CRUD) y JpaSpecificationExecutor para filtros dinámicos.
 */
public interface DepartamentoRepository extends JpaRepository<Departamento, Long>, JpaSpecificationExecutor<Departamento> {
}
