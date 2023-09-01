package org.freitas.vendas.controller;

import org.freitas.vendas.domain.dto.ClienteDto;
import org.freitas.vendas.domain.entity.Cliente;
import org.freitas.vendas.domain.repository.ClienteRepository;
import org.freitas.vendas.exceptions.DatabaseException;
import org.freitas.vendas.service.ResourceNotFoundException;
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
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.freitas.vendas.domain.dto.ClienteDto.fromEntity;
import static org.freitas.vendas.util.ValidationUtils.*;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 30/08/2023
 * {@code @project} spring-vendas
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteRepository repository;

    @Autowired
    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves a ClienteDto object by its ID.
     *
     * @param id the ID of the ClienteDto object to retrieve
     * @return the ResponseEntity containing the ClienteDto object
     * @throws ResourceNotFoundException if the ClienteDto object is not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> getClienteById(@PathVariable(value = "id") String id) {
        Optional<Cliente> cliente = repository.findById(checkId(id));
        return cliente.map(value -> ResponseEntity.ok().body(ClienteDto.fromEntity(value)))
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PostMapping()
    public ResponseEntity<ClienteDto> save(@Valid @RequestBody ClienteDto novoCliente) {
        Cliente cliente = new Cliente();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(novoCliente, cliente);
        Cliente clienteSalvo = repository.save(cliente);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(clienteSalvo.getId()).toUri();
        return ResponseEntity.created(location).body(fromEntity(clienteSalvo));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ClienteDto>> saveAll(@Valid @RequestBody List<ClienteDto> novosClientes) {
        List<Cliente> clientes = novosClientes.stream()
                .map(dto -> {
                    Cliente cliente = new Cliente();
                    ModelMapper modelMapper = new ModelMapper();
                    modelMapper.getConfiguration().setSkipNullEnabled(true);
                    modelMapper.map(dto, cliente);
                    return cliente;
                })
                .collect(Collectors.toList());
        List<Cliente> clientesSalvos = repository.saveAll(clientes);
        List<ClienteDto> dtos = clientesSalvos.stream()
                .map(ClienteDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> update(@Valid @RequestBody ClienteDto clienteAtualizado, @PathVariable @Valid String id) {
        Optional<Cliente> clienteAntigo = repository.findById(checkId(id));
        if (clienteAntigo.isPresent()) {
            Cliente clienteAtual = clienteAntigo.get();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(clienteAtualizado, clienteAtual);
            repository.save(clienteAtual);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(clienteAntigo.get().getId()).toUri();
            return ResponseEntity.ok().location(location).body(fromEntity(clienteAtual));
        } else {
            throw new ResourceNotFoundException(id);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") @Valid String id) {
        try {
            Optional<Cliente> cliente = repository.findById(checkId(id));
            if (cliente.isPresent()) {
                repository.deleteById(checkId(id));
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

/*    @GetMapping()
    public ResponseEntity<Page<ClienteDto>> findAllOrderBy(
            @PageableDefault(size = 20, page = 0, sort = {"id"}) Pageable pageable,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        pageable = validatePageable(pageable);
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
        Page<ClienteDto> page = repository.findAllOrderBy(pageable);
        List<ClienteDto> dtos = page.getContent().stream()
                .map(ClienteDto::new)
                .collect(Collectors.toList());
        Page<ClienteDto> pageDto = new PageImpl<>(dtos, pageable, page.getTotalElements());
        return ResponseEntity.ok().body(pageDto);
        //Teste: http://meu.dominio.interno:8080/api/clientes?sort=id&page=0&size=20&direction=desc
    }*/

    @GetMapping()
    public ResponseEntity<Page<ClienteDto>> findAllOrderBy(Pageable pageable,
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
        Page<ClienteDto> pageResult = repository.findAllOrderBy(pageable);
        List<ClienteDto> dtos = pageResult.getContent().stream()
                .map(ClienteDto::new)
                .collect(Collectors.toList());
        Page<ClienteDto> pageDto = new PageImpl<>(dtos, pageable, pageResult.getTotalElements());
        return ResponseEntity.ok().body(pageDto);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<ClienteDto>> buscaComFiltro(ClienteDto filtro,
            @PageableDefault(size = 20, page = 0)
            Pageable pageable, @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        validarFiltro(filtro);
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
        Example<Cliente> example = Example.of(filtro.toEntity(), exampleMatcher);
        Page<Cliente> page = repository.findAll(example, pageable);
        List<ClienteDto> dtos = page.getContent().stream()
                .map(ClienteDto::fromEntity)
                .collect(Collectors.toList());
        Page<ClienteDto> pageDto = new PageImpl<>(dtos, pageable, page.getTotalElements());
        return ResponseEntity.ok(pageDto);
        //Teste: http://meu.dominio.interno:8080/api/clientes/search?email=example&page=0&sort=id&size=20&direction=desc
    }
}