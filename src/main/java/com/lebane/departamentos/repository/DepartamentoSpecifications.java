package com.lebane.departamentos.repository;

import com.lebane.departamentos.domain.Departamento;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory de Specifications para filtrar Departamentos por disponible, precioMin y precioMax.
 */
public final class DepartamentoSpecifications {

    private DepartamentoSpecifications() {
    }

    /**
     * Construye una Specification que combina los filtros opcionales.
     * Si un parámetro es null, no se aplica ese filtro.
     */
    public static Specification<Departamento> withFiltros(Boolean disponible, Double precioMin, Double precioMax) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (disponible != null) {
                predicates.add(cb.equal(root.get("disponible"), disponible));
            }
            if (precioMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("precio"), precioMin));
            }
            if (precioMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("precio"), precioMax));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
