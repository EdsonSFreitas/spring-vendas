package org.freitas.vendas.validation;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.List;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 04/09/2023
 * {@code @project} spring-vendas
 */

public class NotEmptyCollectionValidator implements ConstraintValidator<NotEmptyCollection, Collection> {

    @Override
    public boolean isValid(Collection value, ConstraintValidatorContext context) {
        return !CollectionUtils.isEmpty(value);
    }


    @Override
    public void initialize(NotEmptyCollection constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}