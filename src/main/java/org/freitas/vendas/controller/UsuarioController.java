package org.freitas.vendas.controller;

import lombok.RequiredArgsConstructor;
import org.freitas.vendas.domain.dto.UsuarioDto;
import org.freitas.vendas.domain.entity.Usuario;
import org.freitas.vendas.service.impl.UsuarioServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/09/2023
 * {@code @project} spring-vendas
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController implements Serializable {
    private static final long serialVersionUID = -7484640256766898249L;

    private final transient UsuarioServiceImpl usuarioService;
    private final transient PasswordEncoder encoder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@Valid @RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = Usuario.builder()
                .login(usuarioDto.getLogin())
                .senha(encoder.encode(usuarioDto.getSenha()))
                .admin(usuarioDto.isAdmin())
                .build();
        try {
            usuarioService.salvar(usuario);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error occurred.");
        }
    }
}