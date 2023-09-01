package org.freitas.vendas.util;

import org.freitas.vendas.service.ResourceNotFoundException;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 01/09/2023
 * {@code @project} spring-vendas
 */

public class ValidationUtils {
    public static Integer checkId(String id) {
        if (!id.matches("^\\d+$")) {
            throw new ResourceNotFoundException("ID must be a positive number. Received: " + id);
        }
        return Integer.parseInt(id);
    }
}