package org.freitas.vendas.domain.dto;

import lombok.Value;
import org.freitas.vendas.domain.dto.ClienteDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link org.freitas.vendas.domain.entity.Pedido}
 */
@Value
public class PedidoDto implements Serializable {
    ClienteDto cliente;
    LocalDate dataPedido;
    BigDecimal total;
}