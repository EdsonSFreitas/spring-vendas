package org.freitas.vendas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 02/09/2023
 * {@code @project} spring-vendas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacoesPedidoDTO {
    private Integer codigo;
    private String cpf;
    private String nomeCliente;
    private BigDecimal total;
    //private LocalDateTime dataPedido;
    private String dataPedido;
    private String status;
    private Set<InformacoesItemPedidoDTO> items;
}