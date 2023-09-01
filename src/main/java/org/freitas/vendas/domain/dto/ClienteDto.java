package org.freitas.vendas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.freitas.vendas.domain.entity.Cliente;
import org.hibernate.validator.constraints.br.CPF;
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
    @CPF(message = "Invalid CPF")
    private String cpf;
    @Email(regexp = ".+@.+\\..+", message = "Email must be a well-formed email address")
    String email;


    public ClienteDto(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.cpf = cliente.getCpf();
    }

    public ClienteDto(ClienteDto dto) {
        this.id = dto.getId();
        this.nome = dto.getNome();
        this.email = dto.getEmail();
        this.cpf = dto.getCpf();
    }

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