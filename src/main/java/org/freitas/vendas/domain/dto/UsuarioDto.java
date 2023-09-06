package org.freitas.vendas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.freitas.vendas.domain.entity.Usuario;

import java.io.Serializable;

/**
 * DTO for {@link Usuario}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto implements Serializable {
    private static final long serialVersionUID = 7793228529381213695L;

    String login;
    String senha;
    boolean admin;
}