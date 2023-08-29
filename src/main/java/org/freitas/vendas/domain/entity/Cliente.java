package org.freitas.vendas.domain.entity;

import lombok.*;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 29/08/2023
 * {@code @project} vendas
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    @EqualsAndHashCode.Include
    private Integer id;
    private String nome;
}