package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    private long id = 0;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос на получение списка всех пользователей.");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен запрос на добавление пользователя: {}", user);
        validateUser(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Получен запрос на обновление пользователя: {}", newUser);
        Long newUserId = newUser.getId();
        if (newUserId == null) {
            log.warn("Id должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUserId)) {
            User oldUser = users.get(newUserId);
            validateUser(newUser);
            users.put(newUserId, newUser);
            log.info("Обновлен пользователь: {}", oldUser);
            return oldUser;
        } else {
            log.warn("Юзер с id = " + newUserId + " не найден");
            throw new NotFoundException("Юзер с id = " + newUserId + " не найден");
        }
    }

    public void validateUser(User user) {
        if (users.values().stream()
                .anyMatch(u -> !u.getId().equals(user.getId()) && u.getEmail().equals(user.getEmail()))) {
            log.warn("Этот имейл уже используется другим пользователем");
            throw new DuplicatedDataException("Этот имейл уже используется другим пользователем");
        }
        if (users.values().stream()
                .anyMatch(u -> !u.getId().equals(user.getId()) && u.getLogin().equals(user.getLogin()))) {
            log.warn("Этот логин уже используется другим пользователем");
            throw new DuplicatedDataException("Этот логин уже используется другим пользователем");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}