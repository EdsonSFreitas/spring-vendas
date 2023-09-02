package org.freitas.vendas.service.impl;

import lombok.RequiredArgsConstructor;
import org.freitas.vendas.domain.dto.ItemPedidoDto;
import org.freitas.vendas.domain.dto.PedidoDto;
import org.freitas.vendas.domain.entity.Cliente;
import org.freitas.vendas.domain.entity.ItemPedido;
import org.freitas.vendas.domain.entity.Pedido;
import org.freitas.vendas.domain.entity.Produto;
import org.freitas.vendas.domain.repository.ClienteRepository;
import org.freitas.vendas.domain.repository.ItemPedidoRepository;
import org.freitas.vendas.domain.repository.PedidoRepository;
import org.freitas.vendas.domain.repository.ProdutoRepository;
import org.freitas.vendas.exceptions.BusinessRuleException;
import org.freitas.vendas.service.PedidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 02/09/2023
 * {@code @project} spring-vendas
 */

@Service
@RequiredArgsConstructor //Instancia todos os construtores e os atributos do construtor de forma automática
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ClienteRepository clienteRepository;


    @Override
    @Transactional
    public Pedido salvar(PedidoDto dto) {
        final Integer idCliente = dto.getCliente();
        Cliente cliente = clienteRepository.
                findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Código de Cliente não encontrado"));
        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setCliente(cliente);
        final Set<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);
        return pedido;
    }

    private Set<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDto> items) {
        if (items.isEmpty()) {
            throw new BusinessRuleException("O pedido deve conter pelo menos um item");
        }
        return items.stream().map(dto -> {
            final Integer idProduto = dto.getProduto();
            Produto produto = produtoRepository.findById(idProduto)
                    .orElseThrow(() -> new BusinessRuleException("Produto não encontrado: " + idProduto));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(dto.getQuantidade());
            itemPedido.setPedido(pedido);
            return itemPedido;
        }).collect(Collectors.toSet());
    }
}