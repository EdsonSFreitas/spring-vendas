package org.freitas.vendas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.freitas.vendas.domain.dto.ClienteDto;
import org.freitas.vendas.domain.entity.Cliente;
import org.freitas.vendas.domain.repository.ClienteRepository;
import org.freitas.vendas.exceptions.DatabaseException;
import org.freitas.vendas.exceptions.ResourceNotFoundException;
import org.freitas.vendas.exceptions.StandardError;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serializable;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.freitas.vendas.domain.dto.ClienteDto.fromEntity;
import static org.freitas.vendas.util.ValidationUtils.checkId;
import static org.freitas.vendas.util.ValidationUtils.validarFiltroClienteDto;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 30/08/2023
 * {@code @project} spring-vendas
 */
@RestController
@Tag(name = "Clientes", description = "Operações relacionadas aos clientes")
@RequestMapping(value = "/api/clientes")
public class ClienteController implements Serializable {
    private static final long serialVersionUID = 8553757501330900962L;

    private final transient ClienteRepository repository;

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
    @Operation(summary = "${rest.cliente.findById}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${rest.response.cliente.found}"),
            @ApiResponse(responseCode = "403", description = "${rest.response.access.denied}"),
            @ApiResponse(responseCode = "404", description = "${rest.response.cliente-notfound}"),
            @ApiResponse(responseCode = "405", description = "${method.controller.notAllowed}"
            )}
    )
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> getClienteById(@PathVariable(value = "id")
                                                     @Parameter(description = "id do cliente") String id) {
        Optional<Cliente> cliente = repository.findById(checkId(id));
        return cliente.map(value -> ResponseEntity.ok().body(ClienteDto.fromEntity(value)))
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    /**
     * @param novoCliente Informações do cliente. Exemplo: {"nome": "Ze das Colves", "cpf": "123456789", "email": "ze@example.com"}
     */
    @Operation(summary = "Registrar novo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${rest.response.cliente.found}"),
            @ApiResponse(responseCode = "405", description = "${method.controller.notAllowed}")}
    )
    @PostMapping()
    public ResponseEntity<ClienteDto> save(
            @Valid @RequestBody ClienteDto novoCliente) {
        Cliente cliente = new Cliente();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(novoCliente, cliente);
        Cliente clienteSalvo = repository.save(cliente);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(clienteSalvo.getId()).toUri();
        return ResponseEntity.created(location).body(fromEntity(clienteSalvo));
    }

    @Operation(summary = "Registrar massivo de novos clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${rest.response.cliente.found}"),
            @ApiResponse(responseCode = "405", description = "${method.controller.notAllowed}")}
    )
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

    @Operation(summary = "Atualizar registro de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${rest.response.cliente.found}"),
            @ApiResponse(responseCode = "405", description = "${method.controller.notAllowed}")}
    )
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


    @Operation(summary = "Excluir registro de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${rest.response.cliente.found}"),
            @ApiResponse(responseCode = "405", description = "${method.controller.notAllowed}")}
    )
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

    @Operation(summary = "Buscar registro de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${rest.response.cliente.found}"),
            @ApiResponse(responseCode = "405", description = "${method.controller.notAllowed}")}
    )
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
        Page<Cliente> pageResult = repository.findAllOrderBy(pageable);
        Page<ClienteDto> pageDto = pageResult.map(ClienteDto::fromEntity); // Mapear para DTOs aqui
        return ResponseEntity.ok().body(pageDto);
    }



    @Operation(summary = "Buscar cliente em todos atributos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${rest.response.cliente.found}"),
            @ApiResponse(responseCode = "405", description = "${method.controller.notAllowed}")}
    )
    @GetMapping("/search")
    public ResponseEntity<Page<ClienteDto>> buscaComFiltro(ClienteDto filtro,
                                                           @PageableDefault(size = 20, page = 0)
                                                           Pageable pageable, @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        validarFiltroClienteDto(filtro);
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String error = "Acesso negado";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(ZonedDateTime.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

}