package org.freitas.vendas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 02/09/2023
 * {@code @project} spring-vendas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDto implements Serializable {
    private static final long serialVersionUID = -9169151759132003742L;
    @NotNull(message = "Produto não pode ser nulo")
    @Positive(message = "Produto não pode ser negativo")
    Integer idProduto;
    @NotNull(message = "Quantidade não pode ser nulo")
    @Positive(message = "Quantidade não pode ser negativo")
    Integer quantidade;
    BigDecimal precoTotal;//new

}