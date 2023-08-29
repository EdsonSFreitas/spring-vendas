package org.freitas.vendas.exceptions;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 29/08/2023
 * {@code @project} vendas
 */

public class ObjetoNaoEncontrado extends RuntimeException {
    public ObjetoNaoEncontrado(Object id, String message) {
        super(message+ " id: " + id);
    }

    public ObjetoNaoEncontrado(String message, Throwable cause) {
        super(message, cause);
    }
}