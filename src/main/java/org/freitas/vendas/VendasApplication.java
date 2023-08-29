package org.freitas.vendas;

import org.freitas.vendas.domain.entity.Cliente;
import org.freitas.vendas.domain.repositorio.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class VendasApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }

    private static void listarTodos(ClienteRepository clienteRepository) {
        Pageable pageable = PageRequest.of(0, 50);
        Page<Cliente> clientes = clienteRepository.findAll(pageable);
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
    }

    @Bean
    public CommandLineRunner init(@Autowired ClienteRepository clienteRepository) {
        return args -> {
            System.out.println("\t LISTA ORIGINAL: ");
            clienteRepository.salvar(new Cliente("Ruby"));
            clienteRepository.salvar(new Cliente("Maribel"));
            clienteRepository.salvar(new Cliente("Bianca XX"));
            clienteRepository.salvar(new Cliente("Bianca YY"));

            listarTodos(clienteRepository);

            final Optional<List<Cliente>> resultBusca = clienteRepository.buscarPorNome("bianca");
            System.out.println(resultBusca.get());

            System.out.println("\t DELETANDO: ");
            clienteRepository.deletarPorId(1);

            listarTodos(clienteRepository);
        };

    }


}