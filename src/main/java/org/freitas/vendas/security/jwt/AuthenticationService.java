package org.freitas.vendas.security.jwt;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.freitas.vendas.domain.dto.AuthenticationRequest;
import org.freitas.vendas.domain.dto.AuthenticationResponse;
import org.freitas.vendas.domain.dto.RegisterRequest;
import org.freitas.vendas.domain.entity.Usuario;
import org.freitas.vendas.domain.enums.Role;
import org.freitas.vendas.domain.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse salvar(@Valid RegisterRequest request) {
        var user = Usuario.builder()
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isEnabled(true)
                .isAccountLocked(false)
                .build();

        repository.findByLogin(request.getLogin()).ifPresent(u -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já existe.");
        });

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .login(user.getLogin())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
        var user = repository.findByLogin(request.getLogin())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .login(user.getLogin())
                .build();
    }
}