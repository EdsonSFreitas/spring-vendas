package org.freitas.vendas.domain.entity;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/09/2023
 * {@code @project} spring-vendas
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "{field.login.obrigatorio}")
    private String login;
    @NotEmpty(message = "{field.senha.obrigatorio}")
    private String senha;

    private boolean admin;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Usuario usuario = (Usuario) o;
        return getId() != null && Objects.equals(getId(), usuario.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}