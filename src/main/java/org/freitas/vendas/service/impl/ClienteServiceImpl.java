package org.freitas.vendas.service.impl;

import org.freitas.vendas.domain.dto.ClienteDto;
import org.freitas.vendas.domain.entity.Cliente;
import org.freitas.vendas.domain.repository.ClienteRepository;
import org.freitas.vendas.exceptions.DatabaseException;
import org.freitas.vendas.exceptions.ResourceNotFoundException;
import org.freitas.vendas.service.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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
@Service
public class ClienteServiceImpl implements ClienteService {

    private final transient ClienteRepository repository;

    @Autowired
    public ClienteServiceImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClienteDto getClienteById(String id) {
        Optional<Cliente> cliente = repository.findById(checkId(id));
        return cliente.map(ClienteDto::fromEntity).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public ClienteDto save(Cliente novoCliente) {
        Cliente cliente = new Cliente();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(novoCliente, cliente);
        Cliente clienteSalvo = repository.save(cliente);
        return fromEntity(clienteSalvo);
    }


    @Override
    public List<ClienteDto> saveAll(List<Cliente> novosClientes) {
        List<Cliente> clientes = novosClientes.stream()
                .map(dto -> {
                    Cliente cliente = new Cliente();
                    ModelMapper modelMapper = new ModelMapper();
                    modelMapper.getConfiguration().setSkipNullEnabled(true);
                    modelMapper.map(dto, cliente);
                    return cliente;
                })
                .toList();
        List<Cliente> clientesSalvos = repository.saveAll(clientes);
        List<ClienteDto> dtos = clientesSalvos.stream()
                .map(ClienteDto::fromEntity)
                .collect(Collectors.toList());
        return dtos;
    }

    @Override
    public ClienteDto update(ClienteDto clienteAtualizado, String id) {
        Optional<Cliente> clienteAntigo = repository.findById(checkId(id));
        if (clienteAntigo.isPresent()) {
            Cliente clienteAtual = clienteAntigo.get();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(clienteAtualizado, clienteAtual);
            repository.save(clienteAtual);
            return fromEntity(clienteAtual);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }


    @Override
    public void delete(String id) {
        try {
            Optional<Cliente> cliente = repository.findById(checkId(id));
            if (cliente.isPresent()) {
                repository.deleteById(checkId(id));
            }
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }


    @Override
    public Page<ClienteDto> findAllOrderBy(Pageable pageable, String direction) {
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
        Page<Cliente> pageResult = repository.findAllOrderBy(pageable);
        return pageResult.map(ClienteDto::fromEntity);
    }


    @Override
    public Page<ClienteDto> buscaComFiltro(ClienteDto filtro,
                                           Pageable pageable, String direction) {
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
        Page<Cliente> page = repository.findAll(example, pageable);
        List<ClienteDto> dtos = page.getContent().stream()
                .map(ClienteDto::fromEntity)
                .toList();
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
        //Teste: http://meu.dominio.interno:8080/api/clientes/search?email=example&page=0&sort=id&size=20&direction=desc
    }

    @Override
    public Optional<Cliente> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Cliente> findAll(Example<Cliente> example, Pageable pageable) {
        return null;
    }


}