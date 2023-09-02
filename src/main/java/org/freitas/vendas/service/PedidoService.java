package org.freitas.vendas.service;

import org.freitas.vendas.domain.dto.PedidoDto;
import org.freitas.vendas.domain.entity.Pedido;
import org.springframework.stereotype.Service;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 02/09/2023
 * {@code @project} spring-vendas
 */
@Service
public interface PedidoService {
    Pedido salvar(PedidoDto dto);
}