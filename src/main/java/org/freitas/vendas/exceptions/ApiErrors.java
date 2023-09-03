package org.freitas.vendas.exceptions;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 03/09/2023
 * {@code @project} spring-vendas
 */

public class ApiErrors {

    @Getter
    private List<String> errors;

    public ApiErrors(String mensagemErro){
        this.errors = Arrays.asList(mensagemErro);
    }
}