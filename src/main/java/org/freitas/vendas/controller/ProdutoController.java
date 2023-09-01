package org.freitas.vendas.controller;

import org.freitas.vendas.domain.dto.ProdutoDto;
import org.freitas.vendas.domain.entity.Produto;
import org.freitas.vendas.domain.repository.ProdutoRepository;
import org.freitas.vendas.exceptions.DatabaseException;
import org.freitas.vendas.exceptions.ResourceNotFoundException;
import org.freitas.vendas.service.ProdutoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.freitas.vendas.domain.dto.ProdutoDto.fromEntity;
import static org.freitas.vendas.util.ValidationUtils.checkId;
import static org.freitas.vendas.util.ValidationUtils.validarFiltroProdutoDto;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 30/08/2023
 * {@code @project} spring-vendas
 */
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController implements Serializable {
    private static final long serialVersionUID = 6178785521518463717L;

    private final ProdutoRepository repository;
    private final ProdutoService service;

    @Autowired
    public ProdutoController(ProdutoRepository repository, ProdutoService service) {
        this.repository = repository;
        this.service = service;
    }

    /**
     * Retrieves a ProdutoDTO object by its ID.
     *
     * @param id the ID of the ProdutoDTO object to retrieve
     * @return the ResponseEntity containing the ProdutoDTO object
     * @throws ResourceNotFoundException if the ProdutoDTO object is not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDto> getClienteById(@PathVariable(value = "id") String id) {
        Optional<Produto> produto = service.getClienteById(checkId(id));
        return produto.map(value -> ResponseEntity.ok().body(ProdutoDto.fromEntity(value)))
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PostMapping()
    public ResponseEntity<ProdutoDto> save(@Valid @RequestBody ProdutoDto novoProduto) {
        Produto produto = new Produto();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(novoProduto, produto);
        Produto produtoSalvo = repository.save(produto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(produtoSalvo.getId()).toUri();
        return ResponseEntity.created(location).body(fromEntity(produtoSalvo));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ProdutoDto>> saveAll(@Valid @RequestBody List<ProdutoDto> novosProdutos) {
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
        List<ProdutoDto> dtos = produtosSalvos.stream()
                .map(ProdutoDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDto> update(@Valid @RequestBody ProdutoDto produtoAtualizado, @PathVariable @Valid String id) {
        Optional<Produto> produtoAntigo = repository.findById(checkId(id));
        if (produtoAntigo.isPresent()) {
            Produto produtoAtual = produtoAntigo.get();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(produtoAtualizado, produtoAtual);
            repository.save(produtoAtual);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(produtoAntigo.get().getId()).toUri();
            return ResponseEntity.ok().location(location).body(fromEntity(produtoAtual));
        } else {
            throw new ResourceNotFoundException(id);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") @Valid String id) {
        try {
            Optional<Produto> produto = repository.findById(checkId(id));
            if (produto.isPresent()) {
                repository.deleteById(checkId(id));
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<Page<ProdutoDto>> findAllOrderBy(Pageable pageable,
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
        Page<ProdutoDto> pageDto = new PageImpl<>(dtos, pageable, pageResult.getTotalElements());
        return ResponseEntity.ok().body(pageDto);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<ProdutoDto>> buscaComFiltro(ProdutoDto filtro,
                                                           @PageableDefault(size = 20, page = 0)
                                                           Pageable pageable, @RequestParam(value = "direction", defaultValue = "asc") String direction) {
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
        Page<ProdutoDto> pageDto = new PageImpl<>(dtos, pageable, page.getTotalElements());
        return ResponseEntity.ok(pageDto);
        //Teste: http://meu.dominio.interno:8080/api/clientes/search?email=example&page=0&sort=id&size=20&direction=desc
    }
}