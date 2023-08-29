package org.freitas.vendas;

import org.freitas.vendas.domain.entity.Cliente;
import org.freitas.vendas.domain.repositorio.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class VendasApplication {

    private static void listagemClientes(ClienteRepository clienteRepository) {
        List<Cliente> clientes;
        System.out.println("\t LISTA DE CLIENTES: ");
        clientes = clienteRepository.findAll();
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(@Autowired ClienteRepository clienteRepository) {
        return args -> {
            System.out.println("\t LISTA ORIGINAL: ");
            clienteRepository.salvar(new Cliente(null, "Freitas"));
            clienteRepository.salvar(new Cliente(null, "Avel"));
            clienteRepository.salvar(new Cliente(null, "Maribel"));
            clienteRepository.salvar(new Cliente(null, "Ruby"));
            List<Cliente> clientes = clienteRepository.findAll();
            for (Cliente cliente : clientes) {
                System.out.println(cliente);
            }

            System.out.println("\t FIND BY ID 1 : ");
            Optional<Cliente> cId = Optional.ofNullable(clienteRepository.buscarPorId(1));
            cId.ifPresent(System.out::println);

            Optional<Cliente> cUpdate = Optional.ofNullable(clienteRepository.buscarPorId(1));
            cUpdate.ifPresent(cliente -> clienteRepository.atualizar(new Cliente(cliente.getId(), "Edson Freitas")));

            listagemClientes(clienteRepository);
            System.out.println("\t DELETE By ID: ");
            Optional<Cliente> cDelete = Optional.ofNullable(clienteRepository.buscarPorId(1));
            cDelete.ifPresent(cliente -> clienteRepository.deletarPorId(cliente.getId()));

            listagemClientes(clienteRepository);
            clienteRepository.buscarPorNome("ruby").ifPresent(System.out::println);
        };

    }


}