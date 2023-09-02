package org.freitas.vendas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 02/09/2023
 * {@code @project} spring-vendas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDto {
    Integer produto;
    Integer quantidade;

}