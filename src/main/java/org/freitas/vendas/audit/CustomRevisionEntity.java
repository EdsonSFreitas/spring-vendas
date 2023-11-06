package org.freitas.vendas.audit;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/11/2023
 * {@code @project} spring-vendas
 */


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import java.util.Objects;


@Entity
@Table(name = "revinfo")
@RevisionEntity(CustomRevisionListener.class)
@Getter
@Setter
@ToString(callSuper = true)
public class CustomRevisionEntity extends DefaultRevisionEntity {
    private String usuario;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomRevisionEntity that)) return false;
        if (!super.equals(o)) return false;

        return Objects.equals(usuario, that.usuario);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (usuario != null ? usuario.hashCode() : 0);
        return result;
    }
}