package org.freitas.vendas.util;

import org.freitas.vendas.domain.dto.ClienteDto;
import org.freitas.vendas.service.ResourceNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 01/09/2023
 * {@code @project} spring-vendas
 */

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static Integer checkId(String id) {
        if (!id.matches("^\\d+$")) {
            throw new ResourceNotFoundException("ID must be a positive number. Received: " + id);
        }
        return Integer.parseInt(id);
    }

    public static Pageable validatePageable(Pageable pageable) {
        if (pageable.getPageSize() > 20) {
            pageable = PageRequest.of(pageable.getPageNumber(), 20);
        }
        return pageable;
    }


    public static void validarFiltro(ClienteDto filtro) {
        if (filtro == null) {
            throw new ResourceNotFoundException("The filter must not be null.");
        }

        if (filtro.getNome() != null && filtro.getNome().length() < 3) {
            throw new ResourceNotFoundException("The 'nome' attribute in the filter must have at least 3 characters: " + filtro.getNome());
        }

        if (filtro.getEmail() != null && filtro.getEmail().length() < 3) {
            throw new ResourceNotFoundException("The 'email' attribute in the filter must have at least 3 characters: " + filtro.getEmail());
        }
    }
}