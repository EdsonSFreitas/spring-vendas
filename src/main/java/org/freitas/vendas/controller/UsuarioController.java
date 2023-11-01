package org.freitas.vendas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.freitas.vendas.domain.dto.DadosAutenticacaoRetorno;
import org.freitas.vendas.domain.dto.UsuarioStatusRetornoDTO;
import org.freitas.vendas.domain.dto.UsuarioStatusUpdateDTO;
import org.freitas.vendas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/v1.0/usuario")
    public ResponseEntity<Page<DadosAutenticacaoRetorno>> findAllUsuarios(
            @PageableDefault(size = 30, page = 0, sort = {"id"})
            Pageable paginacao) {
        var page = service.findAll(paginacao);
        return ResponseEntity.ok(page.map(DadosAutenticacaoRetorno::new));
    }

    @Operation(summary = "Change user status", description = "Block or Expire credential or account an existing user based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Action completed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @RolesAllowed("ADMIN")
    @PatchMapping({"/v1.0/usuario/changestatus", "/v1.1/usuario/changestatus"})
    public ResponseEntity<Optional<UsuarioStatusRetornoDTO>> changeStatusUserById(@RequestBody UsuarioStatusUpdateDTO updateStatus) {
        final Optional<UsuarioStatusRetornoDTO> userStatusRetornoDTO = service.updateUsuario(updateStatus.getId(), updateStatus);
        return ResponseEntity.ok().body(userStatusRetornoDTO);
    }
}