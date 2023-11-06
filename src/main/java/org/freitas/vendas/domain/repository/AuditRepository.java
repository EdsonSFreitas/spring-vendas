package org.freitas.vendas.domain.repository;

import org.freitas.vendas.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/09/2023
 * {@code @project} spring-vendas
 */
@Repository
public interface AuditRepository extends JpaRepository<Usuario, Integer>, RevisionRepository<Usuario, Integer, Long> {

}