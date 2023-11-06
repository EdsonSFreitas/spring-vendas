package org.freitas.vendas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import org.freitas.vendas.domain.entity.Usuario;
import org.freitas.vendas.domain.repository.AuditRepository;
import org.hibernate.envers.AuditReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/09/2023
 * {@code @project} spring-vendas
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Audit")
public class AuditController implements Serializable {
    @Serial
    private static final long serialVersionUID = -7484640256766898249L;

    private final transient AuditRepository repository;

    public AuditController(AuditRepository repository) {
        this.repository = repository;
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/v1.0/revisions/{id}")
    public ResponseEntity<List<String>> revisions(@PathVariable("id") Integer id ) {
        final List<String> list = repository.findRevisions(id)
                .stream()
                .map(Object::toString)
                .toList();

        return ResponseEntity.ok(list);
    }

}