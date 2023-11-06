package org.freitas.vendas.audit;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/11/2023
 * {@code @project} spring-vendas
 */

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomRevisionListener implements RevisionListener {

    @Override
    public void newRevision(final Object entity) {
        CustomRevisionEntity revision = (CustomRevisionEntity) entity;
        revision.setUsuario("User without authentication");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                revision.setUsuario(userDetails.getUsername());
            }
        }
    }
}