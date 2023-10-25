package org.freitas.vendas.service;

import org.freitas.vendas.domain.dto.UsuarioStatusRetornoDTO;
import org.freitas.vendas.domain.dto.UsuarioStatusUpdateDTO;
import org.freitas.vendas.domain.entity.Usuario;
import org.freitas.vendas.domain.repository.UsuarioRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Classe com logica de autenticacao do projeto e implementa UserDetailsService
 * Sprint ira carregar automaticamente essa classe por ter @Service e implementar UserDetailsSerivce
 * @author Edson da Silva Freitas
 * {@code @created} 21/05/2023
 * {@code @project} api
 */
@Service
public class UsuarioService {


    private final UsuarioRepository repository;
    private final ModelMapper modelMapper;

    @Autowired
    public UsuarioService(UsuarioRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    //@Override
    public Optional<UsuarioStatusRetornoDTO> updateUsuario(Integer id, UsuarioStatusUpdateDTO updateStatus) {
        final Usuario userToChange = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: "+ id));

        // Dessa forma usando if é a mais simples, menos verbosa, fácil de entender e funciona mas mantive a versão mais complexa para fins de testes:
		if (updateStatus.getAccountExpiration() != null) {
            userToChange.setAccountExpiration(updateStatus.getAccountExpiration());
        }
        if (updateStatus.getIsAccountLocked() != null) {
            userToChange.setAccountLocked(updateStatus.getIsAccountLocked());
        }
        if (updateStatus.getCredentialsExpiration() != null) {
            userToChange.setCredentialsExpiration(updateStatus.getCredentialsExpiration());
        }
        if (updateStatus.getIsEnabled() != null) {
            userToChange.setEnabled(updateStatus.getIsEnabled());
        }
        if (updateStatus.getRole() != null) {
            userToChange.setRole(updateStatus.getRole());
        }

/*
        TypeMap<UsuarioStatusUpdateDTO, Usuario> typeMap =
                modelMapper.getTypeMap(UsuarioStatusUpdateDTO.class, Usuario.class);

        if (typeMap == null) {
            modelMapper.addMappings(new PropertyMap<UsuarioStatusUpdateDTO, Usuario>() {
                @Override
                protected void configure() {
                    when(Conditions.isNotNull()).map().setAccountExpiration(source.getAccountExpiration());
                    when(Conditions.isNotNull()).map().setAccountLocked(source.getIsAccountLocked());
                    when(Conditions.isNotNull()).map().setCredentialsExpiration(source.getCredentialsExpiration());
                    when(Conditions.isNotNull()).map().setEnabled(source.getIsEnabled());
                    when(Conditions.isNotNull()).map().setRole(source.getRole());
                }
            });
        }

        modelMapper.map(updateStatus, userToChange);
*/

        final Usuario user = repository.save(userToChange);

        UsuarioStatusRetornoDTO retornoDTO = new UsuarioStatusRetornoDTO(
                user.getId(),
                user.getLogin(),
                user.getAccountExpiration(),
                user.isAccountLocked(),
                user.getCredentialsExpiration(),
                user.isEnabled(),
                user.getRole()
        );
        return Optional.of(retornoDTO);
    }
}