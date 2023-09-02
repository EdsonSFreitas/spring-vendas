package org.freitas.vendas.service;

import org.freitas.vendas.domain.dto.ProdutoDto;
import org.freitas.vendas.domain.entity.Produto;
import org.freitas.vendas.domain.repository.ProdutoRepository;
import org.freitas.vendas.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.freitas.vendas.domain.dto.ProdutoDto.fromEntity;
import static org.freitas.vendas.util.ValidationUtils.validarFiltroProdutoDto;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 01/09/2023
 * {@code @project} spring-vendas
 */
@Service
public class ProdutoService implements Serializable {
    private static final long serialVersionUID = -5593119741941655232L;

    private final transient ProdutoRepository repository;

    @Autowired
    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Optional<Produto> getProdutoById(Integer id) {
        return repository.findById(id);
    }

    public ProdutoDto save(ProdutoDto novoProduto) {
        Produto produto = new Produto();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(novoProduto, produto);
        Produto produtoSalvo = repository.save(produto);
        ProdutoDto produtoDtoSalvo = new ProdutoDto();
        modelMapper.map(produtoSalvo, produtoDtoSalvo);
        return produtoDtoSalvo;
    }

    public List<ProdutoDto> saveAll(List<ProdutoDto> novosProdutos) {
        List<Produto> produtos = novosProdutos.stream()
                .map(dto -> {
                    Produto produto = new Produto();
                    ModelMapper modelMapper = new ModelMapper();
                    modelMapper.getConfiguration().setSkipNullEnabled(true);
                    modelMapper.map(dto, produto);
                    return produto;
                })
                .collect(Collectors.toList());
        List<Produto> produtosSalvos = repository.saveAll(produtos);
        return produtosSalvos.stream()
                .map(ProdutoDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ProdutoDto update(ProdutoDto produtoAtualizado, Integer id) {
        Produto produtoAntigo = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(produtoAtualizado, produtoAntigo);
        return fromEntity(repository.save(produtoAntigo));
    }

    public void deleteById(Integer id) {
        Optional<Produto> produto = repository.findById(id);
        produto.ifPresent(value -> repository.deleteById(value.getId()));
    }

    public Page<ProdutoDto> findAllOrderBy(Pageable pageable,
                                           @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        Sort.Direction sortDirection;
        if (direction.equalsIgnoreCase("desc")) {
            sortDirection = Sort.Direction.DESC;
        } else {
            sortDirection = Sort.Direction.ASC;
        }
        Sort sort = Sort.by(pageable.getSort().stream()
                .map(order -> Sort.Order.by(order.getProperty()).with(sortDirection))
                .collect(Collectors.toList()));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        if (pageable.getPageSize() > 100) {
            pageable = PageRequest.of(pageable.getPageNumber(), 100, sort);
        }
        Page<ProdutoDto> pageResult = repository.findAllOrderBy(pageable);
        List<ProdutoDto> dtos = pageResult.getContent().stream()
                .map(ProdutoDto::new)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, pageResult.getTotalElements());
    }

    public Page<ProdutoDto> buscaComFiltro(
            ProdutoDto filtro, Pageable pageable, String direction) {
        validarFiltroProdutoDto(filtro);
        Sort.Direction sortDirection;
        if (direction.equalsIgnoreCase("desc")) {
            sortDirection = Sort.Direction.DESC;
        } else {
            sortDirection = Sort.Direction.ASC;
        }
        Sort sort = Sort.by(pageable.getSort().stream()
                .map(order -> Sort.Order.by(order.getProperty()).with(sortDirection))
                .collect(Collectors.toList()));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        if (pageable.getPageSize() > 100) {
            pageable = PageRequest.of(pageable.getPageNumber(), 100, sort);
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Produto> example = Example.of(filtro.toEntity(), exampleMatcher);
        Page<Produto> page = repository.findAll(example, pageable);
        List<ProdutoDto> dtos = page.getContent().stream()
                .map(ProdutoDto::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
        //Teste: http://meu.dominio.interno:8080/api/clientes/search?email=example&page=0&sort=id&size=20&direction=desc
    }


}