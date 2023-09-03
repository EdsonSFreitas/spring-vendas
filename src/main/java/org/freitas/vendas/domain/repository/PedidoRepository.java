package org.freitas.vendas.domain.repository;

import org.freitas.vendas.domain.entity.Pedido;
import org.freitas.vendas.domain.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 29/08/2023
 * {@code @project} vendas
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    @Query("SELECT p FROM Pedido p left join fetch p.itens WHERE p.id = :id")
    Optional<Pedido> findByIdFetchItems(@Param("id") Integer id);
}