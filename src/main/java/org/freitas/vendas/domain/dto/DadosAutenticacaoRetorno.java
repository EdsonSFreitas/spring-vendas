package org.freitas.vendas.domain.dto;

import org.freitas.vendas.domain.entity.Usuario;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 21/05/2023
 * {@code @project} api
 */
public record DadosAutenticacaoRetorno(Integer id, String login, String role) {

    public DadosAutenticacaoRetorno(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin(), usuario.getRole().toString());
    }
}