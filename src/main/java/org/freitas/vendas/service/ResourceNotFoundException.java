package org.freitas.vendas.service;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 31/08/2023
 * {@code @project} spring-vendas
 */

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Object id) {
        super("Resource not found. Id " + id);
    }
}