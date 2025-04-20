package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {
    UserController userController = new UserController();

    @Test
    void validateUserEmailIsBlank() {
        User user = User.builder()
                .email("")
                .login("validLogin")
                .name("Valid Name")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void validateUserEmailMissingAtSymbol() {
        User user = User.builder()
                .email("invalidEmail")
                .login("validLogin")
                .name("Valid Name")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void validateUserLoginIsBlank() {
        User user = User.builder()
                .email("valid@ya.ru")
                .login("")
                .name("Valid Name")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void validateUserLoginContainsSpaces() {
        User user = User.builder()
                .email("valid@ya.ru")
                .login("invalid Login")
                .name("Valid Name")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void validateUserNameIsNullUsesLogin() {
        User user = User.builder()
                .email("valid@ya.ru")
                .login("validLogin")
                .name(null)
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertDoesNotThrow(() -> userController.validateUser(user));
        assertEquals("validLogin", user.getName());
    }

    @Test
    void validateUserBirthdayIsInFuture() {
        User user = User.builder()
                .email("valid@ya.ru")
                .login("validLogin")
                .name("Valid Name")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    void validateUserIsValid() {
        User user = User.builder()
                .email("valid@ya.ru")
                .login("validLogin")
                .name("Valid Name")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertDoesNotThrow(() -> userController.validateUser(user));
    }
}
