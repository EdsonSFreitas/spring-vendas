package org.freitas.vendas.exceptions;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 31/08/2023
 * {@code @project} spring-vendas
 */

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Object id) {
        super("Resource not found. " + id);
    }

    public ResourceNotFoundException(String message, Object id) {
        super(message + " " + id);
    }
}