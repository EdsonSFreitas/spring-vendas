package org.freitas.vendas.security;

import lombok.RequiredArgsConstructor;
import org.freitas.vendas.security.jwt.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 05/09/2023
 * {@code @project} spring-vendas
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public SecurityFilter filter() {
        return new SecurityFilter(exceptionResolver);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        if(activeProfile.equals("test")) {
            http.csrf().disable().authorizeHttpRequests().anyRequest().permitAll();
        }else {
            http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                            .requestMatchers(
                                    "/api/v1/auth/**",
                                    "/v2/api-docs",
                                    "/v3/api-docs",
                                    "/v3/api-docs/**",
                                    "/swagger-resources",
                                    "/swagger-resources/**",
                                    "/configuration/ui",
                                    "/configuration/security",
                                    "/swagger-ui/**",
                                    "/webjars/**",
                                    "/swagger-ui.html",
                                    "/manage/**"
                            ).permitAll()
                            .requestMatchers(regexMatcher(HttpMethod.POST, "/api/v1.\\d+/auth/.*")).permitAll()
                            .requestMatchers("/api/v1.0/pedidos/**").hasAnyRole("USER", "ADMIN")
                            .anyRequest().authenticated()
                            .and())
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    //.authenticationProvider(authenticationProvider)
                    .addFilterBefore(filter(), UsernamePasswordAuthenticationFilter.class)
                    .headers(headers -> headers.frameOptions().disable())
                    .csrf(csrf -> csrf
                            .ignoringRequestMatchers(
                                    AntPathRequestMatcher.antMatcher("/h2-console/**" )))
                    .csrf().disable();
        }

        return http.build();
    }
}