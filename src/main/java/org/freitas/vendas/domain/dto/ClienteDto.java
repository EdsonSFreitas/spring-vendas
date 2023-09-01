package org.freitas.vendas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.freitas.vendas.domain.entity.Cliente;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * DTO for {@link org.freitas.vendas.domain.entity.Cliente}
 */
//@Value
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto implements Serializable {
    private static final long serialVersionUID = -2232353118201999305L;

    Integer id;
    @NotBlank
    String nome;
    @Email(regexp = ".+@.+\\..+", message = "Email must be a well-formed email address")
    String email;


    public ClienteDto(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
    }

    public ClienteDto(ClienteDto dto) {
        this.id = dto.getId();
        this.nome = dto.getNome();
        this.email = dto.getEmail();
    }

    public static ClienteDto fromEntity(Cliente cliente) {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setId(cliente.getId());
        clienteDto.setNome(cliente.getNome());
        clienteDto.setEmail(cliente.getEmail());
        return clienteDto;
    }

    public String getEmail() {
        if (email == null) {
            return "";
        }
        return email;
    }
}