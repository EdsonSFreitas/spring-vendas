package org.freitas.vendas.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.freitas.vendas.domain.entity.Cliente;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO for {@link org.freitas.vendas.domain.entity.Cliente}
 */
//@Value
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -2232353118201999305L;
    Integer id;
    @Schema(example = "Zé das Colves")
    @NotBlank
    String nome;
    @Schema(example = "ze@example.com")
    @Email(regexp = ".+@.+\\..+", message = "{field.email.invalido}")
    String email;
    @Schema(example = "123456789")
    @CPF(message = "{field.cpf.cliente.invalido}")
    private String cpf;

    public static ClienteDto fromEntity(Cliente cliente) {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setId(cliente.getId());
        clienteDto.setNome(cliente.getNome());
        clienteDto.setEmail(cliente.getEmail());
        clienteDto.setCpf(cliente.getCpf());
        return clienteDto;
    }

    public Cliente toEntity() {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setCpf(cpf);
        return cliente;
    }
}