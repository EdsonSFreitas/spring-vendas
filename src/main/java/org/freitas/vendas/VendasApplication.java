package org.freitas.vendas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:messages_en_US.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
@PropertySource(value = "classpath:messages_pt_BR.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
@SpringBootApplication
public class VendasApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(VendasApplication.class);
    }
}