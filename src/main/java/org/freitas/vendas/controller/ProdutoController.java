package org.freitas.vendas.controller;

import org.freitas.vendas.domain.dto.ProdutoDto;
import org.freitas.vendas.domain.entity.Produto;
import org.freitas.vendas.domain.repository.ProdutoRepository;
import org.freitas.vendas.exceptions.DatabaseException;
import org.freitas.vendas.exceptions.ResourceNotFoundException;
import org.freitas.vendas.service.ProdutoService;
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

    private final transient ProdutoService service;

    @Autowired
    public ProdutoController(ProdutoRepository repository, ProdutoService service) {
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
        Optional<Produto> produto = service.getProdutoById(checkId(id));
        return produto.map(value -> ResponseEntity.ok().body(ProdutoDto.fromEntity(value)))
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PostMapping()
    public ResponseEntity<ProdutoDto> save(@Valid @RequestBody ProdutoDto novoProduto) {
        ProdutoDto produtoSalvo = service.save(novoProduto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(produtoSalvo.getId()).toUri();
        return ResponseEntity.created(location).body(produtoSalvo);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ProdutoDto>> saveAll(@Valid @RequestBody List<ProdutoDto> novosProdutos) {
        List<ProdutoDto> dtos = service.saveAll(novosProdutos);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDto> update(@Valid @RequestBody ProdutoDto produtoAtualizado, @PathVariable @Valid String id) {
        final Optional<Produto> produtoById = service.getProdutoById(checkId(id));
        Produto produtoDtoSalvo = service.update(produtoAtualizado, produtoById.get().getId()).toEntity();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(produtoById.get().getId()).toUri();
        return ResponseEntity.ok().location(location).body(fromEntity(produtoDtoSalvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") @Valid String id) {
        try {
            Optional<Produto> produto = service.getProdutoById(checkId(id));
            if (produto.isPresent()){
                    service.deleteById(produto.get().getId());
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
        return ResponseEntity.ok().body(service.findAllOrderBy(pageable, direction));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProdutoDto>> buscaComFiltro(ProdutoDto filtro,
            @PageableDefault(size = 100, page = 0)
            Pageable pageable, @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        return ResponseEntity.ok(service.buscaComFiltro(filtro, pageable, direction));
        //Teste: http://meu.dominio.interno:8080/api/clientes/search?email=example&page=0&sort=id&size=20&direction=desc
    }
}