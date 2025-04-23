package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginUniquenessValidator.class)
public @interface LoginUniqueness {

    String message() default "Этот логин уже используется другим пользователем";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}