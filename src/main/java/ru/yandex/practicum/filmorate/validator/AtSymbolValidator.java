package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtSymbolValidator implements ConstraintValidator<AtSymbol, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.contains("@");
    }
    
}