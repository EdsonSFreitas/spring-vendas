package org.freitas.vendas.domain.repository;

import org.freitas.vendas.domain.entity.Cliente;
import org.freitas.vendas.domain.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 29/08/2023
 * {@code @project} vendas
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    @Query(value = "SELECT c FROM Cliente c left join fetch c.pedidos WHERE c.id = :id")
    //Equivale a "SELECT * FROM tb_cliente tc LEFT JOIN tb_pedido tp ON tc.id = tp.cliente_id WHERE tc.id = :id"
    Cliente findClienteFetchPedidos(@Param("id") Integer id);

    Optional<Page<Cliente>> findByNomeLikeIgnoreCase(String nome, Pageable pageable);
    Optional<List<Cliente>> findByNomeLikeIgnoreCase(String nome);
    List<Cliente> findByNomeOrId(String nome, Integer id);

    @Query(value = "SELECT id,nome FROM tb_cliente c WHERE c.id = :id", nativeQuery = true)
    Cliente buscaPorId(@Param("id") Integer id);

}