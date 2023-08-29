package org.freitas.vendas.config;

import org.freitas.vendas.domain.entity.Cliente;
import org.freitas.vendas.domain.repositorio.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 29/08/2023
 * {@code @project} spring-vendas
 */
@Configuration
@Profile("dev")
public class TestConfig implements CommandLineRunner {

    private final ClienteRepository clienteRepository;

    @Autowired
    public TestConfig(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    private static void listarTodos(ClienteRepository clienteRepository,
                                    @RequestParam(required = false) Pageable pageable) {
        if (pageable == null) pageable = PageRequest.of(0, 20);
        Page<Cliente> clientes = clienteRepository.findAll(pageable);
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        clienteRepository.save(new Cliente("Ruby"));
        clienteRepository.save(new Cliente("Maribel"));
        clienteRepository.save(new Cliente("Bianca XX"));
        clienteRepository.save(new Cliente("Bianca YY"));
        listarTodos(clienteRepository, null);
    }
}