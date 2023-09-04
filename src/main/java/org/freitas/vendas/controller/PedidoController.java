package org.freitas.vendas.controller;

import org.freitas.vendas.domain.dto.AtualizaStatusPedido;
import org.freitas.vendas.domain.dto.InformacoesItemPedidoDTO;
import org.freitas.vendas.domain.dto.InformacoesPedidoDTO;
import org.freitas.vendas.domain.dto.PedidoDto;
import org.freitas.vendas.domain.entity.ItemPedido;
import org.freitas.vendas.domain.entity.Pedido;
import org.freitas.vendas.domain.enums.StatusPedido;
import org.freitas.vendas.exceptions.ResourceNotFoundException;
import org.freitas.vendas.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 02/09/2023
 * {@code @project} spring-vendas
 */
@RestController
@RequestMapping(value = "/api/pedidos", consumes = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements Serializable {

    private static final long serialVersionUID = -7985726640015917002L;
    private final transient PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public InformacoesPedidoDTO getPedidoById(@PathVariable Integer id) {
        return service.obterPedidoCompleto(id)
                .map(this::converterPedidoDto)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Integer id,
                             @RequestBody @Valid AtualizaStatusPedido dto) {
        final String novoStatus = dto.getNovoStatus();
        service.atualizaStatus(id, StatusPedido.valueOf(novoStatus));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer save(@RequestBody @Valid PedidoDto dto) {
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

    private InformacoesPedidoDTO converterPedidoDto(Pedido pedido) {
        //Com Mapper return modelMapper.map(pedido, InformacoesPedidoDTO.class);
        return InformacoesPedidoDTO.builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converterItemPedidoDto(pedido.getItens()))
                .build();
    }

    private Set<InformacoesItemPedidoDTO> converterItemPedidoDto(Set<ItemPedido> itens) {
        //return itens.stream().map(p -> modelMapper.map(p, InformacoesItemPedidoDTO.class)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(itens)) {
            return Collections.emptySet();
        }
        return itens.stream().map(item -> InformacoesItemPedidoDTO.builder()
                .descricaoProduto(item.getProduto().getDescricao())
                .precoUnitario(item.getProduto().getPreco())
                .quantidade(item.getQuantidade()).build()).collect(Collectors.toSet());
    }
}