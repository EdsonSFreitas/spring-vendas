package org.freitas.vendas.service.impl;

import org.freitas.vendas.domain.entity.Usuario;
import org.freitas.vendas.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 05/09/2023
 * {@code @project} spring-vendas
 */
@Service
public class UsuarioServiceImpl implements UserDetailsService {

    @Autowired
    @Lazy
    private PasswordEncoder encoder;

    private final UsuarioRepository repository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Usuario salvar(@Valid Usuario usuarioDto) {
        return repository.save(usuarioDto);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Usuario usuario = repository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("{usuario.notfound.banco}"));
        String[] roles = usuario.isAdmin() ? new String[]{"ADMIN", "USER"} : new String[]{"USER"};

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }
}