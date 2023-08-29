package org.freitas.vendas.domain.repositorio;

import org.freitas.vendas.domain.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Optional<Page<Cliente>> findByNomeLikeIgnoreCase(String nome, Pageable pageable);
    Optional<List<Cliente>> findByNomeLikeIgnoreCase(String nome);
    List<Cliente> findByNomeOrId(String nome, Integer id);

    @Query(value = "SELECT id,nome FROM tb_cliente c WHERE c.id = :id", nativeQuery = true)
    Cliente buscaPorId(@Param("id") Integer id);

}