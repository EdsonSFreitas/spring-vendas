package org.freitas.vendas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.freitas.vendas.domain.dto.AuthenticationRequest;
import org.freitas.vendas.domain.dto.AuthenticationResponse;
import org.freitas.vendas.domain.dto.RegisterRequest;
import org.freitas.vendas.security.jwt.AuthenticationService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;


/**
 * @author Edson da Silva Freitas
 * {@code @created} 06/09/2023
 * {@code @project} spring-vendas
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController implements Serializable {
    @Serial
    private static final long serialVersionUID = -7484640256766898249L;
    private final transient AuthenticationService service;
    private final transient MessageSource messageSource;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> salvar(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse response = service.salvar(request);
            AuthenticationResponse convertResponse = AuthenticationResponse.builder()
                    .token(response.getToken())
                    .login(response.getLogin())
                    .timestamp(ZonedDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(convertResponse);
        } catch (ResponseStatusException ex) {
            String errorMessage = messageSource.getMessage("field.security.db.userNotAllowed", null, LocaleContextHolder.getLocale());
            AuthenticationResponse errorResponse = AuthenticationResponse.builder()
                    .timestamp(ZonedDateTime.now())
                    .status(ex.getStatusCode().value())
                    .login(request.getLogin())
                    .error(errorMessage)
                    .build();
            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> autenticar(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = service.authenticate(request);
        AuthenticationResponse convertResponse = AuthenticationResponse.builder()
                .token(response.getToken())
                .login(response.getLogin())
                .timestamp(ZonedDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(convertResponse);
    }
}