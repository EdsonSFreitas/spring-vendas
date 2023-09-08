package org.freitas.vendas.exceptions;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 07/09/2023
 * {@code @project} spring-vendas
 */

public class PasswordDoesNotMatchedException extends RuntimeException{
    public PasswordDoesNotMatchedException(String message) {
        super(message);
    }
}