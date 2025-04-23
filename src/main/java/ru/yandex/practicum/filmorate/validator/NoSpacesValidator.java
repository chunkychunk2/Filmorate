package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoSpacesValidator implements ConstraintValidator<NoSpaces, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !value.contains(" ");
    }
}