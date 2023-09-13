package org.freitas.vendas.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 04/09/2023
 * {@code @project} spring-vendas
 */
@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = {NotEmptyCollectionValidator.class})
public @interface NotEmptyCollection {
    String message() default "{NotEmptyCollection.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}