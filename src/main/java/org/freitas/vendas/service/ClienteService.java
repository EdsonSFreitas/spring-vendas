package org.freitas.vendas.service;

import org.freitas.vendas.domain.dto.ClienteDto;
import org.freitas.vendas.domain.entity.Cliente;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 01/11/2023
 * {@code @project} spring-vendas
 */

public interface ClienteService {

    ClienteDto getClienteById(String id);

    ClienteDto save(Cliente novoCliente);

    List<ClienteDto> saveAll(List<Cliente> novosClientes);

    ClienteDto update(ClienteDto clienteAtualizado, String id);

    void delete(String id);

    Page<ClienteDto> findAllOrderBy(Pageable pageable, String direction);

    Page<ClienteDto> buscaComFiltro(ClienteDto filtro, Pageable pageable, String direction);

    Optional<Cliente> findById(Integer integer);

    void deleteById(Integer integer);

    Page<Cliente> findAll(Example<Cliente> example, Pageable pageable);
}