package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@RequiredArgsConstructor
public class LoginUniquenessValidator implements ConstraintValidator<LoginUniqueness, String> {

    private final UserStorage userStorage;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userStorage.findAll().stream()
                .noneMatch(u -> u.getLogin().equals(value));
    }

}