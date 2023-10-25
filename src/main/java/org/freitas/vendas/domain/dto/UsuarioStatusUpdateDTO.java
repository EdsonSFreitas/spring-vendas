package org.freitas.vendas.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.freitas.vendas.domain.enums.Role;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
// Classe não foi criada como record devido ao ModelMapper para evitar campos null no request body
public class UsuarioStatusUpdateDTO {
    private Integer id;
    private LocalDateTime accountExpiration;
    private Boolean isAccountLocked;
    private LocalDateTime credentialsExpiration;
    private Boolean isEnabled;
    private Role role;
}