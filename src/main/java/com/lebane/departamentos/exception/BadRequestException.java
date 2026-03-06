package com.lebane.departamentos.exception;

/**
 * Petición inválida (ej. id con formato incorrecto en path).
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
