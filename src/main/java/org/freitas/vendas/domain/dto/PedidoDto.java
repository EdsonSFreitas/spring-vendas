package org.freitas.vendas.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.freitas.vendas.validation.NotEmptyCollection;

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
    @NotNull(message = "{field.cod.cliente.obrigatorio}")
    @Positive(message = "{field.cod.cliente.negativo}")
    Integer idCliente;
    @NotNull(message = "{field.total.obrigatorio}")
    @Digits(integer = 10, fraction = 2, message = "{field.total.invalido}")
    BigDecimal total;
    @NotEmptyCollection
    List<ItemPedidoDto> items;
}