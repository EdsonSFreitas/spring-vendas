package org.freitas.vendas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 07/09/2023
 * {@code @project} spring-vendas
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredentialDTO {
    private String login;
    private String password;
}