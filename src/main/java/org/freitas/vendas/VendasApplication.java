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
            System.out.println("\t LISTA ORIGINAL: **********************************************");
            clienteRepository.save(new Cliente("Ruby"));
            clienteRepository.save(new Cliente("Maribel"));
            clienteRepository.save(new Cliente("Bianca XX"));
            clienteRepository.save(new Cliente("Bianca YY"));

            listarTodos(clienteRepository);
            System.out.println("\t FIND BY NOME LIKE PAGEABLE: **********************************************");
            Pageable pageable = PageRequest.of(0, 50);
            final Optional<Page<Cliente>> resultBusca = clienteRepository.findByNomeLikeIgnoreCase("%Bianca%", pageable);
            resultBusca.ifPresent(page -> {
                 /*       System.out.println("Total de elementos encontrados: " + page.getTotalElements());
                        System.out.println("Total de páginas: " + page.getTotalPages());
                        System.out.println("Número da página atual: " + page.getNumber());
                        System.out.println("Tamanho da página: " + page.getSize());*/
                System.out.println("Clientes encontrados:");
                page.forEach(cliente -> {
                    System.out.println("ID: " + cliente.getId() + ", Nome: " + cliente.getNome());
                });
            });
            System.out.println("\t FIND BY NOME OR ID: **********************************************");
            final List<Cliente> resultList = clienteRepository.findByNomeOrId("%Maribel%", 4);
            resultList.forEach(System.out::println);

            System.out.println("\t FIND BY NOME LIKE LIST: **********************************************");
            Optional<List<Cliente>> lisClientes =clienteRepository.findByNomeLikeIgnoreCase("%Bianca%");
            lisClientes.ifPresent(c -> {
                c.forEach(System.out::println);
            });

            /*            System.out.println("\t DELETANDO: **********************************************");
            clienteRepository.deleteById(1);*/
        };
    }
}