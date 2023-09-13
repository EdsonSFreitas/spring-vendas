package org.freitas.vendas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.freitas.vendas.domain.enums.Role;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 11/09/2023
 * {@code @project} spring-vendas
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String login;
    private String password;
    private Role role;
}