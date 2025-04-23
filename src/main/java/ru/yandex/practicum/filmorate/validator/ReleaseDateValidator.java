package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return !value.isBefore(MIN_RELEASE_DATE);

    }
}