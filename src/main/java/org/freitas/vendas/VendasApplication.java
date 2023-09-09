package org.freitas.vendas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:messages_en_US.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
@PropertySource(value = "classpath:messages_pt_BR.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
@SpringBootApplication
public class VendasApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }
}