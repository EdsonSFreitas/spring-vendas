package org.freitas.vendas.exceptions;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 31/08/2023
 * {@code @project} spring-vendas
 */

public class DatabaseException extends RuntimeException {

    private static final long serialVersionUID = 7416249776246764573L;

    public DatabaseException(String msg) {
        super(msg);
    }
}