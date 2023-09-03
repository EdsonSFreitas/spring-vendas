package org.freitas.vendas.domain.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/09/2023
 * {@code @project} spring-vendas
 */
@Getter
@Setter
public class AtualizaStatusPedido {
    private String novoStatus;
}