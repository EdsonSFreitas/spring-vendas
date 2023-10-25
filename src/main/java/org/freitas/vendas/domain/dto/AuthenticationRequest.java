package org.freitas.vendas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 07/09/2023
 * {@code @project} spring-vendas
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    private String login;
    private String password;
}