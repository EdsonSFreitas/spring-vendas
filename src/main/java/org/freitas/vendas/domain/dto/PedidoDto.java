package org.freitas.vendas.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link org.freitas.vendas.domain.entity.Pedido}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PedidoDto implements Serializable {
    private static final long serialVersionUID = -7926241217490407133L;
    @NotNull(message = "Codigo do cliente não pode ser nulo")
    @Positive(message = "Codigo do cliente não pode ser negativo")
    Integer idCliente;
    @NotNull(message = "Campo Total não pode ser nulo")
    @Digits(integer = 10, fraction = 2, message = "Campo Total com valor invalido ou nulo")
    BigDecimal total;
    List<ItemPedidoDto> items;
}