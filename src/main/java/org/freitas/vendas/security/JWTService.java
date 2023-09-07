package org.freitas.vendas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.freitas.vendas.VendasApplication;
import org.freitas.vendas.domain.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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

    public String generateToken(Usuario usuario) {
        long expString = Long.parseLong(expiration);
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(expString);
        Date expirationDateDate = Date.from(expirationDate.atZone(java.time.ZoneId.systemDefault()).toInstant());

        return Jwts
                .builder()
                .setSubject(usuario.getLogin())
                .setExpiration(expirationDateDate)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try{
            final Claims claims = getClaims(token);
            Date dateExpiration =  claims.getExpiration();
            final LocalDateTime data = dateExpiration.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            return !LocalDateTime.now().isAfter(data);
        }catch (ExpiredJwtException e) {
            return false;
        }
    }

    public String getLoginUser(String token) throws ExpiredJwtException{
        return getClaims(token).getSubject();
    }

    public static void main(String[] args) {
        final ConfigurableApplicationContext contextoApp = SpringApplication.run(VendasApplication.class);
        JWTService jwtService = contextoApp.getBean(JWTService.class);
        Usuario usuario = Usuario.builder().login("Freitas").admin(false).build();
        String token = jwtService.generateToken(usuario);
        System.out.println(token);
        System.out.println();
        System.out.println("Token valid? " + jwtService.validateToken(token));
        System.out.println("User Token: " + jwtService.getLoginUser(token));
    }
}