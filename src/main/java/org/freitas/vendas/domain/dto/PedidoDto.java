package org.freitas.vendas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link org.freitas.vendas.domain.entity.Pedido}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDto implements Serializable {
    private static final long serialVersionUID = -7926241217490407133L;
    Integer cliente;
    BigDecimal total;
    List<ItemPedidoDto> items;
}