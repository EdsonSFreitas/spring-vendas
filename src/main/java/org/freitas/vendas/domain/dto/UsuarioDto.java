package org.freitas.vendas.domain.dto;

import lombok.*;
import org.freitas.vendas.domain.entity.Usuario;
import org.freitas.vendas.security.PasswordComplexity;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * DTO for {@link Usuario}
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UsuarioDto implements Serializable {
    private static final long serialVersionUID = 7793228529381213695L;

    @NotEmpty(message = "{field.login.obrigatorio}")
    private String login;
    @NotEmpty(message = "{field.senha.obrigatorio}")
    @PasswordComplexity(minLength = 3, requireLowerCase = true, requireUpperCase = true, requireSpecialChar = true, requireNumber = true, message = "{field.senha.complexidade}")
    private String senha;
    boolean admin;
}