package org.freitas.vendas.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.freitas.vendas.domain.entity.Usuario;
import org.freitas.vendas.domain.repository.UsuarioRepository;
import org.freitas.vendas.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 07/09/2023
 * {@code @project} spring-vendas
 */
@Service
public class JWTService {
    @Value("${security.jwt.expiration}")
    private String expiration;
    @Value("${security.jwt.secret}")
    private String secret;

    private final UsuarioRepository usuarioRepository;

    public JWTService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String generateToken(Usuario usuario) {
        Optional<Usuario> userBanco = usuarioRepository.findByLogin(usuario.getLogin());

        if (userBanco.isPresent()) {
            long expString = Long.parseLong(expiration);
            LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(expString);
            Date expirationDateDate = Date.from(expirationDate.atZone(java.time.ZoneId.systemDefault()).toInstant());

            return Jwts
                    .builder()
                    .setIssuer("freitas.com.br")
                    .setSubject(usuario.getLogin())
                    .setExpiration(expirationDateDate)
                    .claim("id", userBanco.get().getId())
                    .claim("aud", "freitas.com.br")
                    .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, secret)
                    .compact();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{usuario.notfound.db}");
        }
    }

    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            final Claims claims = getClaims(token);
            Date dateExpiration = claims.getExpiration();
            final LocalDateTime data = dateExpiration.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            return !LocalDateTime.now().isAfter(data);
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    public String getLoginUser(String token) throws ExpiredJwtException {
        return getClaims(token).getSubject();
    }
}