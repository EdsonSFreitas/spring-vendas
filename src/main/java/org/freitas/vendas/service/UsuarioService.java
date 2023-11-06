package org.freitas.vendas.service;

import org.freitas.vendas.domain.dto.UsuarioStatusRetornoDTO;
import org.freitas.vendas.domain.dto.UsuarioStatusUpdateDTO;
import org.freitas.vendas.domain.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 01/11/2023
 * {@code @project} spring-vendas
 */

public interface UsuarioService {
    Page<Usuario> findAll(Pageable paginacao);

    Optional<UsuarioStatusRetornoDTO> updateUsuario(Integer id, UsuarioStatusUpdateDTO updateStatus);

    void deleteUser(Integer id);

    Optional<Usuario> findById(Integer id);
}