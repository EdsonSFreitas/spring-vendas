package org.freitas.vendas.service.impl;

import org.freitas.vendas.domain.entity.Usuario;
import org.freitas.vendas.domain.repository.UsuarioRepository;
import org.freitas.vendas.exceptions.PasswordDoesNotMatchedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 05/09/2023
 * {@code @project} spring-vendas
 */
@Service
public class UsuarioServiceImpl implements UserDetailsService {

    private final UsuarioRepository repository;
    private final MessageSource messageSource;

    @Autowired
    @Lazy
    private PasswordEncoder encoder;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository repository, MessageSource messageSource) {
        this.repository = repository;
        this.messageSource = messageSource;
    }

    @Transactional
    public void salvar(@Valid Usuario usuario) {
        repository.findByLogin(usuario.getLogin()).ifPresent(u -> {
            String errorMessage = messageSource.getMessage("field.security.db.userNotAllowed", null, LocaleContextHolder.getLocale());
            throw new ResponseStatusException(HttpStatus.CONFLICT, errorMessage);
        });
        repository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String errorMessage = messageSource.getMessage("usuario.notfound.db", null, LocaleContextHolder.getLocale());
        final Usuario usuario = repository.findByLogin(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage));
        String[] roles = usuario.isAdmin() ? new String[]{"ADMIN", "USER"} : new String[]{"USER"};

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }

    public UserDetails authenticate(Usuario usuario) {
        final UserDetails user = loadUserByUsername(usuario.getLogin());
        if (encoder.matches(usuario.getSenha(), user.getPassword())) {
            return user;
        }
        String errorMessage = messageSource.getMessage("security.jwt.passwordDoesNotMatched", null, LocaleContextHolder.getLocale());
        throw new PasswordDoesNotMatchedException(errorMessage);
    }
}