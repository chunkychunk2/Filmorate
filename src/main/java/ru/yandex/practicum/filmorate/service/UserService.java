package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User create(@Valid User user) {
        return userStorage.create(user);
    }

    public User update(@Valid User user) {
        return userStorage.update(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User addFriend(long userId, long newFriendId) {
        log.info("Добавление пользователя в друзья");
        User user = userStorage.findById(userId);
        User newFriend = userStorage.findById(newFriendId);

        if (user.getFriends().contains(newFriendId) && newFriend.getFriends().contains(userId)) {
            log.warn("У пользователя уже есть друг с таким id");
        }
        userStorage.addFriend(user, newFriendId);
        userStorage.addFriend(newFriend, userId);
        log.info("Пользователь <{}> добавил в друзья пользователя <{}>", user.getName(), newFriend.getName());

        return user;
    }

    public User deleteFromFriends(long userId, long userToDeleteId) {
        log.info("Удаление пользователя из списка друзей");
        User user = userStorage.findById(userId);
        User userToDelete = userStorage.findById(userToDeleteId);

        userStorage.removeFriend(user, userToDeleteId);
        userStorage.removeFriend(userToDelete, userId);
        log.info("Пользователь <{}> удалил из друзей пользователя <{}>", user.getName(), userToDelete.getName());

        return user;
    }

    public Collection<User> getCommonFriends(int userId, int friendId) {
        log.info("Получение списка общих друзей пользователей");
        validationUserId(userId, friendId);

        Collection<User> commonFriends = new ArrayList<>();
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);

        if ((user.getFriends() == null || user.getFriends().isEmpty()) || (friend.getFriends() == null
                || friend.getFriends().isEmpty())) {
            log.info("У пользователя нет друзей");
        }

        log.debug("Формирование списка c id общих друзей пользователей с id = {} и id = {}", user.getId(), friend.getId());

        Set<Long> userFriends = new HashSet<>(user.getFriends());
        userFriends.retainAll(friend.getFriends());


        Collection<User> allUsers = userStorage.findUsersByIds(userFriends);
        log.debug("Формирование списка общих друзей");
        for (User us : allUsers) {
            if (userFriends.contains(us.getId())) {
                commonFriends.add(us);
            }
        }
        log.info("Список общих друзей пользователей <{}> и <{}> успешно сформирован", user.getName(), friend.getName());
        return commonFriends;
    }

    public Collection<User> getUsersFriends(long userId) {
        log.info("Получение всех друзей пользователя");
        User user = userStorage.findById(userId);
        log.debug("Формирование списка друзей пользователя с id = <{}>", user.getId());
        Collection<User> userFriends = userStorage.findUsersByIds(user.getFriends());
        log.info("Список друзей пользователя <{}> успешно сформирован", user.getName());
        return userFriends;
    }

    public void validationUserId(long userId1, long userId2) {
        log.debug("Валидация id пользователей userId1 = {}, userId2 = {}", userId1, userId2);
        if (userId1 == userId2) {
            throw new ValidationException("id пользователей не могут быть одинаковыми");
        }
        log.debug("Валидация прошла успешно");
    }
}
