package org.freitas.vendas.controller;

import lombok.RequiredArgsConstructor;
import org.freitas.vendas.domain.dto.CredentialDTO;
import org.freitas.vendas.domain.dto.TokenDTO;
import org.freitas.vendas.domain.dto.UsuarioDto;
import org.freitas.vendas.domain.entity.Usuario;
import org.freitas.vendas.exceptions.PasswordDoesNotMatchedException;
import org.freitas.vendas.security.jwt.JWTService;
import org.freitas.vendas.service.impl.UsuarioServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final transient JWTService jwtService;
    private final transient MessageSource messageSource;

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

    @PostMapping("/auth")
    public TokenDTO autenticar(@Valid @RequestBody CredentialDTO credentials) {
        try {
            final Usuario usuario = Usuario.builder()
                    .login(credentials.getLogin())
                    .senha(credentials.getPassword())
                    .build();
            usuarioService.authenticate(usuario);
            final String token = jwtService.generateToken(usuario);
            return new TokenDTO(usuario.getLogin(), token);
        } catch (UsernameNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }catch (PasswordDoesNotMatchedException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    messageSource.getMessage("security.jwt.passwordDoesNotMatched", null, LocaleContextHolder.getLocale()));
        }
    }

}