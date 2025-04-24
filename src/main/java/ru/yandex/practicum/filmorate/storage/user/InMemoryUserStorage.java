package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    private long id = 1;

    @Override
    public Collection<User> findAll() {
        log.info("Получен запрос на получение списка всех пользователей.");
        return users.values();
    }


    @Override
    public Collection<User> findUsersByIds(Set<Long> ids) {
        log.info("Получен запрос на получение пользователей по id: {}", ids);
        return users.entrySet().stream()
                .filter(entry -> ids.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public User findById(long id) {
        log.info("Получен запрос на получение пользователя по id: {}", id);
        log.debug("Поиск пользователя с id = <{}>", id);
        if (id <= 0) {
            throw new ValidationException("id пользователя не может быть меньше значения <1>");
        }
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        return user;
    }

    @Override
    public User create(User user) {
        log.info("Получен запрос на добавление пользователя: {}", user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @Override
    public User update(User newUser) {
        log.info("Получен запрос на обновление пользователя: {}", newUser);
        Long newUserId = newUser.getId();
        if (newUserId == null) {
            log.warn("Id должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        User oldUser = users.get(newUserId);
        if (oldUser == null) {
            log.warn("Юзер с id = " + newUserId + " не найден");
            throw new NotFoundException("Юзер с id = " + newUserId + " не найден");
        }
        users.put(newUserId, newUser);
        log.info("Обновлен пользователь: {}", oldUser);
        return oldUser;
    }

    @Override
    public void addFriend(User user, long friendId) {
        user.getFriends().add(friendId);
    }

    @Override
    public void removeFriend(User user, long friendId) {
        user.getFriends().remove(friendId);
    }

}

