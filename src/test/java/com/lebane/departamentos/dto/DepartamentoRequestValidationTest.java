package com.lebane.departamentos.dto;

import com.lebane.departamentos.domain.Moneda;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitarios de validación Bean Validation sobre DepartamentoRequest.
 */
class DepartamentoRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("request válido no tiene violaciones")
    void requestValido_sinViolaciones() {
        DepartamentoRequest request = requestValido();
        Set<ConstraintViolation<DepartamentoRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("titulo en blanco genera violación")
    void tituloEnBlanco_violacion() {
        DepartamentoRequest request = requestValido();
        request.setTitulo("   ");
        Set<ConstraintViolation<DepartamentoRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("titulo")
                && v.getMessage().toLowerCase().contains("título"));
    }

    @Test
    @DisplayName("titulo null genera violación")
    void tituloNull_violacion() {
        DepartamentoRequest request = requestValido();
        request.setTitulo(null);
        Set<ConstraintViolation<DepartamentoRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("titulo"));
    }

    @Test
    @DisplayName("precio null genera violación")
    void precioNull_violacion() {
        DepartamentoRequest request = requestValido();
        request.setPrecio(null);
        Set<ConstraintViolation<DepartamentoRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("precio"));
    }

    @Test
    @DisplayName("precio menor o igual a 0 genera violación")
    void precioCeroONegativo_violacion() {
        DepartamentoRequest request = requestValido();
        request.setPrecio(0.0);
        Set<ConstraintViolation<DepartamentoRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("precio"));
    }

    @Test
    @DisplayName("moneda null genera violación")
    void monedaNull_violacion() {
        DepartamentoRequest request = requestValido();
        request.setMoneda(null);
        Set<ConstraintViolation<DepartamentoRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("moneda"));
    }

    @Test
    @DisplayName("metrosCuadrados null genera violación")
    void metrosCuadradosNull_violacion() {
        DepartamentoRequest request = requestValido();
        request.setMetrosCuadrados(null);
        Set<ConstraintViolation<DepartamentoRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("metrosCuadrados"));
    }

    @Test
    @DisplayName("metrosCuadrados menor o igual a 0 genera violación")
    void metrosCuadradosCero_violacion() {
        DepartamentoRequest request = requestValido();
        request.setMetrosCuadrados(0.0);
        Set<ConstraintViolation<DepartamentoRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("metrosCuadrados"));
    }

    private static DepartamentoRequest requestValido() {
        DepartamentoRequest r = new DepartamentoRequest();
        r.setTitulo("Depto válido");
        r.setDescripcion("Descripción");
        r.setPrecio(150000.0);
        r.setMoneda(Moneda.USD);
        r.setMetrosCuadrados(45.0);
        r.setDireccion("Av. Corrientes 123");
        r.setDisponible(true);
        return r;
    }
}
