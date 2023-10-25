package org.freitas.vendas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.freitas.vendas.domain.dto.ClienteDto;
import org.freitas.vendas.domain.entity.Cliente;
import org.freitas.vendas.exceptions.DatabaseException;
import org.freitas.vendas.exceptions.ResourceNotFoundException;
import org.freitas.vendas.service.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.freitas.vendas.domain.dto.ClienteDto.fromEntity;
import static org.freitas.vendas.util.ValidationUtils.checkId;
import static org.freitas.vendas.util.ValidationUtils.validarFiltroClienteDto;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 30/08/2023
 * {@code @project} spring-vendas
 */
@RestController
@RequestMapping("/api/v1.0/clientes")
@Tag(name = "Clientes")
public class ClienteController implements Serializable {
    @Serial
    private static final long serialVersionUID = 8553757501330900962L;

    private final transient ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
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
        Optional<Cliente> cliente = service.findById(checkId(id));
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
    public ResponseEntity<ClienteDto> save(@Valid @RequestBody ClienteDto novoCliente) {
        Cliente cliente = new Cliente();
        //TODO tirar esse modelMapper daqui
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(novoCliente, cliente);
        final ClienteDto clienteSalvo = service.save(cliente);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(clienteSalvo.getId()).toUri();
        return ResponseEntity.created(location).body(clienteSalvo);
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
                .toList();
        final List<ClienteDto> clientesSalvos = service.saveAll(clientes);
        List<ClienteDto> dtos = clientesSalvos.stream()
                .map(x -> fromEntity(x.toEntity()))
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
    }


    @Operation(summary = "Atualizar registro de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${rest.response.cliente.found}"),
            @ApiResponse(responseCode = "405", description = "${method.controller.notAllowed}")}
    )
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> update(@Valid @RequestBody ClienteDto clienteAtualizado, @PathVariable @Valid String id) {
        Optional<Cliente> clienteAntigo = service.findById(checkId(id));
        if (clienteAntigo.isPresent()) {
            Cliente clienteAtual = clienteAntigo.get();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(clienteAtualizado, clienteAtual);
            service.save(clienteAtual);
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
            Optional<Cliente> cliente = service.findById(checkId(id));
            if (cliente.isPresent()) {
                service.deleteById(cliente.get().getId());
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
                .toList());
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        if (pageable.getPageSize() > 100) {
            pageable = PageRequest.of(pageable.getPageNumber(), 100, sort);
        }
        final Page<ClienteDto> pageResult = service.findAllOrderBy(pageable, direction);
        return ResponseEntity.ok().body(pageResult);
    }


    @Operation(summary = "Buscar cliente em todos atributos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${rest.response.cliente.found}"),
            @ApiResponse(responseCode = "405", description = "${method.controller.notAllowed}")}
    )
    @GetMapping("/search")
    public ResponseEntity<Page<ClienteDto>> buscaComFiltro(
            ClienteDto filtro,
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
                .toList());
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        if (pageable.getPageSize() > 100) {
            pageable = PageRequest.of(pageable.getPageNumber(), 100, sort);
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Cliente> example = Example.of(filtro.toEntity(), exampleMatcher);
        Page<Cliente> page = service.findAll(example, pageable);
        List<ClienteDto> dtos = page.getContent().stream()
                .map(ClienteDto::fromEntity)
                .toList();
        Page<ClienteDto> pageDto = new PageImpl<>(dtos, pageable, page.getTotalElements());
        return ResponseEntity.ok(pageDto);
        //Teste: http://meu.dominio.interno:8080/api/clientes/search?email=example&page=0&sort=id&size=20&direction=desc
    }


}