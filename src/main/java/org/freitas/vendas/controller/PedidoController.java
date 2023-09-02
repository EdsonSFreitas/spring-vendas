package org.freitas.vendas.controller;

import org.freitas.vendas.domain.dto.PedidoDto;
import org.freitas.vendas.domain.entity.Pedido;
import org.freitas.vendas.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 02/09/2023
 * {@code @project} spring-vendas
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController implements Serializable {

    private static final long serialVersionUID = -7985726640015917002L;
    private final transient PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer save(@RequestBody PedidoDto dto) {
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

}