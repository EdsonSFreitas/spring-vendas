package org.freitas.vendas.service;

import org.freitas.vendas.domain.dto.PedidoDto;
import org.freitas.vendas.domain.entity.Pedido;
import org.freitas.vendas.domain.enums.StatusPedido;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 02/09/2023
 * {@code @project} spring-vendas
 */
@Service
public interface PedidoService {
    Pedido salvar(PedidoDto dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);
}