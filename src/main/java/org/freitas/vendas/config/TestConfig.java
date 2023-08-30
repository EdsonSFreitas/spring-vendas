package org.freitas.vendas.config;

import org.freitas.vendas.domain.entity.Cliente;
import org.freitas.vendas.domain.entity.ItemPedido;
import org.freitas.vendas.domain.entity.Pedido;
import org.freitas.vendas.domain.entity.Produto;
import org.freitas.vendas.domain.repository.ClienteRepository;
import org.freitas.vendas.domain.repository.ItemPedidoRepository;
import org.freitas.vendas.domain.repository.PedidoRepository;
import org.freitas.vendas.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 29/08/2023
 * {@code @project} spring-vendas
 */
@Configuration
@Profile("dev")
public class TestConfig implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoRepository pedidoRepository;

    @Autowired
    public TestConfig(ClienteRepository clienteRepository, ProdutoRepository produtoRepository, ItemPedidoRepository itemPedidoRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.pedidoRepository = pedidoRepository;
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
        Cliente c1 = new Cliente("Ruby");
        Cliente c2 = new Cliente("Avel");
        Cliente c3 = new Cliente("Maribel");
        Cliente c4 = new Cliente("Bianca Souza");
        Cliente c5 = new Cliente("Bianca Xim");
        Produto prod1 = Produto.builder().descricao("Produto 1").preco(BigDecimal.valueOf(10)).build();
        Pedido ped1 = Pedido.builder().cliente(c1).dataPedido(LocalDate.now()).total(BigDecimal.valueOf(100)).build();
        ItemPedido ip1 = ItemPedido.builder().pedido(ped1).produto(prod1).quantidade(1).build();
        ped1.addItemPedido(ip1);
        clienteRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5));
        produtoRepository.saveAll(Arrays.asList(prod1));
        pedidoRepository.saveAll(Arrays.asList(ped1));
        itemPedidoRepository.saveAll(Arrays.asList(ip1));
        listarTodos(clienteRepository, null);

        /*System.out.println("Cliente: "+ped1.getCliente() +
                " Pedido: "+ped1.getDataPedido()+
                " Item Pedido: "+ip1.getProduto().getDescricao()+
                " Total: "+ped1.getTotal());*/

        /*System.out.println("Iniciando fetch para obter os pedidos a partir do cliente");
        final Cliente clienteFetchPedidos = clienteRepository.findClienteFetchPedidos(1);
        System.out.println(clienteFetchPedidos);
       System.out.println(clienteFetchPedidos.getPedidos());
*/
       pedidoRepository.findByCliente(c1).forEach(p -> System.out.println("Pedidos do cliente id 1: "+p));

    }
}