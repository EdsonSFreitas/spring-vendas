package org.freitas.vendas.domain.dto;

import org.freitas.vendas.domain.enums.Role;

import java.time.LocalDateTime;

public record UsuarioStatusRetornoDTO(
        Integer id, String login, LocalDateTime accountExpiration, Boolean isAccountLocked,
        LocalDateTime credentialsExpiration, Boolean isEnabled, Role role) {
    public UsuarioStatusRetornoDTO {
    }
}