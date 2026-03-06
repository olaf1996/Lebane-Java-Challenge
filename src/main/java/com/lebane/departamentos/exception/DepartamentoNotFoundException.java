package com.lebane.departamentos.exception;

/**
 * Se lanza cuando no existe un departamento con el id solicitado (ej. en PUT).
 */
public class DepartamentoNotFoundException extends RuntimeException {

    public DepartamentoNotFoundException(Long id) {
        super("Departamento no encontrado con id: " + id);
    }
}
