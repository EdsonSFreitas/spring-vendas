package org.freitas.vendas.service;

import org.freitas.vendas.domain.entity.Produto;
import org.freitas.vendas.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

import static org.freitas.vendas.util.ValidationUtils.checkId;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 01/09/2023
 * {@code @project} spring-vendas
 */
@Service
public class ProdutoService implements Serializable {
    private static final long serialVersionUID = -5593119741941655232L;

    private final ProdutoRepository repository;

    @Autowired
    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Optional<Produto> getClienteById(Integer id) {
        return repository.findById(id);
    }
}