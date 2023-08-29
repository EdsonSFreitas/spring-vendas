package org.freitas.vendas.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 29/08/2023
 * {@code @project} vendas
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Produto {
    @EqualsAndHashCode.Include
    private Integer id;
    private String descricao;
    private BigDecimal preco;
}