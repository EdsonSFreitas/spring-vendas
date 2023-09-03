package org.freitas.vendas.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 29/08/2023
 * {@code @project} vendas
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;

    @Column(name = "total", length = 20, precision = 20, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<ItemPedido> itens = new HashSet<>();

    public BigDecimal getTotalPedido() {//new
        BigDecimal sum = BigDecimal.ZERO;
        for (ItemPedido itemPedido : itens) {
            sum = sum.add(itemPedido.getSubTotal());
        }
        return sum;
    }

    public void addItemPedido(ItemPedido itemPedido) {
        Optional.ofNullable(this.itens).ifPresent(p -> p.add(itemPedido));
    }

    public void removeItemPedido(ItemPedido itemPedido) {
        Optional.ofNullable(this.itens).ifPresent(p -> p.remove(itemPedido));
    }

    public Set<ItemPedido> getItens() {
        if (itens == null) {
            this.itens = new HashSet<>();
        }
        return Collections.unmodifiableSet(itens);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}