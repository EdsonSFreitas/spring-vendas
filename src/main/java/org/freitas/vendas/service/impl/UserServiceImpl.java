package org.freitas.vendas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 05/09/2023
 * {@code @project} spring-vendas
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    @Lazy
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.equals("testeuser")) {
            throw new UsernameNotFoundException("Username not found.");
        }

        return User
                .builder()
                .username("testeuser")
                .password(encoder.encode("testesenha"))
                .roles("USER", "ADMIN")
                .build();
    }
}