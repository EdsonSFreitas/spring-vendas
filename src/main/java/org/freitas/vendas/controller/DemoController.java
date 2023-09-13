package org.freitas.vendas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 11/09/2023
 * {@code @project} spring-vendas
 */
@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {
    @GetMapping
    public ResponseEntity<String> testSecureEndpoint() {
        return ResponseEntity.ok("Accessing Secure endpoint!");
    }

}