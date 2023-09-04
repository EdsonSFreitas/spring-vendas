package org.freitas.vendas.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.freitas.vendas.domain.entity.Produto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 01/09/2023
 * {@code @project} spring-vendas
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoDto implements Serializable {
    private static final long serialVersionUID = 5105059052176361094L;
    private Integer id;

    @NotBlank(message = "Descrição não pode ser nula")
    private String descricao;

    @NotNull(message = "Preço não pode ser nulo")
    @PositiveOrZero(message = "Preço não pode ser negativo")
    @Digits(integer = 10, fraction = 2, message = "Preço com valor invalido ou nulo")
    private BigDecimal preco;

    public ProdutoDto(Produto produto) {
        this.id = produto.getId();
        this.descricao = produto.getDescricao();
        this.preco = produto.getPreco();
    }

    public ProdutoDto(ProdutoDto dto) {
        this.id = dto.getId();
        this.descricao = dto.getDescricao();
        this.preco = dto.getPreco();
    }

    public static ProdutoDto fromEntity(Produto produto) {
        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setId(produto.getId());
        produtoDto.setDescricao(produto.getDescricao());
        produtoDto.setPreco(produto.getPreco());
        return produtoDto;
    }

    public Produto toEntity() {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        return produto;
    }
}