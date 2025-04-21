package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @Test
    void validateUserEmailIsBlank() {
        User user = new User();
        user.setEmail("");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> inMemoryUserStorage.validateUser(user));
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void validateUserEmailMissingAtSymbol() {
        User user  = new User();
        user.setEmail("invalidEmail");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> inMemoryUserStorage.validateUser(user));
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void validateUserLoginIsBlank() {
        User user = new User();
        user.setEmail("valid@ya.ru");
        user.setLogin("");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> inMemoryUserStorage.validateUser(user));
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void validateUserLoginContainsSpaces() {
        User user = new User();
        user.setEmail("valid@ya.ru");
        user.setLogin("invalid Login");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> inMemoryUserStorage.validateUser(user));
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void validateUserNameIsNullUsesLogin() {
        User user  = new User();
        user.setEmail("valid@ya.ru");
        user.setLogin("validLogin");
        user.setName(null);
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertDoesNotThrow(() -> inMemoryUserStorage.validateUser(user));
        assertEquals("validLogin", user.getName());
    }

    @Test
    void validateUserBirthdayIsInFuture() {
        User user = new User();
        user.setEmail("valid@ya.ru");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception = assertThrows(ValidationException.class, () -> inMemoryUserStorage.validateUser(user));
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    void validateUserIsValid() {
        User user = new User();
        user.setEmail("valid@ya.ru");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertDoesNotThrow(() -> inMemoryUserStorage.validateUser(user));
    }
}
