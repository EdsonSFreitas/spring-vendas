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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.freitas.vendas.domain.dto.ClienteDto.fromEntity;

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

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> getClienteById(@PathVariable(value = "id") Integer id) {
        Optional<Cliente> cliente = repository.findById(id);
        return cliente.map(value -> ResponseEntity.ok().body(ClienteDto.fromEntity(value)))
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PostMapping()
    public ResponseEntity<ClienteDto> save(@RequestBody ClienteDto novoCliente) {
        Cliente cliente = new Cliente();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(novoCliente, cliente);
        Cliente clienteSalvo = repository.save(cliente);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(clienteSalvo.getId()).toUri();
        return ResponseEntity.created(location).body(fromEntity(clienteSalvo));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> update(@RequestBody ClienteDto clienteAtualizado, @PathVariable Integer id) {
        Optional<Cliente> clienteAntigo = repository.findById(id);
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
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Integer id) {
        try {
            Optional<Cliente> cliente = repository.findById(id);
            if (cliente.isPresent()) {
                repository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<Page<ClienteDto>> findAllOrderBy(
            @PageableDefault(size = 20, page = 0, sort = {"id"}) Pageable pageable,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        if (pageable.getPageSize() > 20) {
            pageable = PageRequest.of(pageable.getPageNumber(), 20);
        }

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

        // Converte cada Cliente em ClienteDto
        List<ClienteDto> dtos = page.getContent().stream()
                .map(ClienteDto::new)
                .collect(Collectors.toList());

// Cria uma nova Page contendo os objetos ClienteDto
        Page<ClienteDto> pageDto = new PageImpl<>(dtos, pageable, page.getTotalElements());

        return ResponseEntity.ok().body(pageDto);
        //Teste: http://meu.dominio.interno:8080/api/clientes?sort=id&page=0&size=20&direction=desc
    }

}