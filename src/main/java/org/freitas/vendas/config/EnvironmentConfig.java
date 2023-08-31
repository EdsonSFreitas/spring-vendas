package org.freitas.vendas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Credencias de acesso ao banco a partir de variaveis de ambiente na IDE ou S.O
 * @author Edson da Silva Freitas
 * {@code @created} 31/08/2023
 * {@code @project} spring-vendas
 */
@Configuration
public class EnvironmentConfig {
  @Value("${DB_USER}")
  private String dbUser;

  @Value("${DB_PASS}")
  private String dbPassword;
}