package org.freitas.vendas.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
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
@Table(name = "tb_cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Integer id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;
    private String email;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private Set<Pedido> pedidos = new HashSet<>();


    public void addPedido(Pedido pedido) {
        Optional.ofNullable(this.pedidos).ifPresent(p -> p.add(pedido));
    }

    public void removePedido(Pedido pedido) {
        Optional.ofNullable(this.pedidos).ifPresent(p -> p.remove(pedido));
    }

    public Set<Pedido> getPedidos() {
        return Collections.unmodifiableSet(pedidos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id) && Objects.equals(nome, cliente.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }
}