package org.freitas.vendas.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.freitas.vendas.domain.enums.Role;
import org.freitas.vendas.security.PasswordComplexity;

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
    @NotBlank(message = "{field.login.obrigatorio}")
    private String login;
    @PasswordComplexity(minLength = 3, requireLowerCase = true, requireUpperCase = true, requireSpecialChar = true, requireNumber = true, message = "{field.senha.complexidade}")
    private String password;
    private Role role = Role.ROLE_USER;
}