package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class BaseValidationTest {

    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    protected final Validator validator = validatorFactory.getValidator();
}
