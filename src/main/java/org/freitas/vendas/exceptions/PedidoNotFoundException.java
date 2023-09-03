package org.freitas.vendas.exceptions;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/09/2023
 * {@code @project} spring-vendas
 */

public class PedidoNotFoundException extends RuntimeException {
    public PedidoNotFoundException() {
        super("Pedido não encontado.");
    }
}