package org.freitas.vendas.domain.repository;

import org.freitas.vendas.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 30/08/2023
 * {@code @project} spring-vendas
 */

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}