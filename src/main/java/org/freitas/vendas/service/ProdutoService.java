package org.freitas.vendas.service;

import org.freitas.vendas.domain.dto.ProdutoDto;
import org.freitas.vendas.domain.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 01/11/2023
 * {@code @project} spring-vendas
 */

public interface ProdutoService {
    Optional<Produto> getProdutoById(Integer id);

    ProdutoDto save(ProdutoDto novoProduto);

    List<ProdutoDto> saveAll(List<ProdutoDto> novosProdutos);

    ProdutoDto update(ProdutoDto produtoAtualizado, Integer id);

    void deleteById(Integer id);

    Page<ProdutoDto> findAllOrderBy(Pageable pageable,
                                    @RequestParam(value = "direction", defaultValue = "asc") String direction);

    Page<ProdutoDto> buscaComFiltro(
            ProdutoDto filtro, Pageable pageable, String direction);
}