package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService() {
        this.userStorage = new InMemoryUserStorage();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User addFriend(long userId, long newFriendId) {
        log.info("Добавление пользователя в друзья");
        User user = findById(userId);
        User newFriend = findById(newFriendId);

        if (user.getFriends().contains(newFriendId) && newFriend.getFriends().contains(userId)) {
            throw new ValidationException("У пользователя уже есть друг с таким id");
        }
        user.getFriends().add(newFriendId);
        newFriend.getFriends().add(userId);
        log.info("Пользователь <{}> добавил в друзья пользователя <{}>", user.getName(), newFriend.getName());

        return user;
    }

    public User deleteFromFriends(long userId, long userToDeleteId) {
        log.info("Удаление пользователя из списка друзей");
        User user = findById(userId);
        User userToDelete = findById(userToDeleteId);

        user.getFriends().remove(userToDeleteId);
        userToDelete.getFriends().remove(userId);
        log.info("Пользователь <{}> удалил из друзей пользователя <{}>", user.getName(), userToDelete.getName());

        return user;
    }

    public Collection<User> getMutualFriends(int userId, int friendId) {
        log.info("Получение списка общих друзей пользователей");
        validationUserId(userId, friendId);

        Collection<User> mutualFriends = new ArrayList<>();
        User user = findById(userId);
        User friend = findById(friendId);

        if ((user.getFriends() == null || user.getFriends().isEmpty()) || (friend.getFriends() == null
                || friend.getFriends().isEmpty())) {
            throw new ValidationException("У пользователя нет друзей");
        }

        log.debug("Формирование списка c id общих друзей пользователей с id = {} и id = {}", user.getId(), friend.getId());
        List<Long> mutualFriendsId = user.getFriends()
                .stream()
                .filter(x -> friend.getFriends().contains(x))
                .collect(ArrayList::new, List::add, List::addAll);

        Collection<User> allUsers = userStorage.findAll();
        log.debug("Формирование списка общих друзей");
        for (User us : allUsers) {
            if (mutualFriendsId.contains(us.getId())) {
                mutualFriends.add(us);
            }
        }
        log.info("Список общих друзей пользователей <{}> и <{}> успешно сформирован", user.getName(), friend.getName());
        return mutualFriends;
    }

    public Collection<User> getUsersFriends(long userId) {
        log.info("Получение всех друзей пользователя");
        Collection<User> userFriends = new ArrayList<>();
        User user = findById(userId);

        Collection<User> allUsers = userStorage.findAll();

        log.debug("Формирование списка друзей пользователя с id = <{}>", user.getId());
        for (User us : allUsers) {
            if (user.getFriends().contains(us.getId()) && us.getFriends().contains(userId)) {
                userFriends.add(us);
            }
        }
        log.info("Список друзей пользователя <{}> успешно сформирован", user.getName());
        return userFriends;
    }

    public User findById(long userId) {
        log.debug("Поиск пользоватлея с id = <{}>", userId);
        if (userId <= 0) {
            throw new ValidationException("id пользователя не может быть меньше значения <1>");
        }

        return userStorage.findAll()
                .stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("пользователь с id = " + userId + "не найден"));
    }

    public void validationUserId(long userId1, long userId2) {
        log.debug("Валидация id пользователей userId1 = {}, userId2 = {}", userId1, userId2);
        if (userId1 <= 0 || userId2 <= 0) {
            throw new ValidationException("id пользователя не может быть меньше значения <1>");
        }
        if (userId1 == userId2) {
            throw new ValidationException("id пользователей не могут быть одинаковыми");
        }
        log.debug("Валидация прошла успешно");
    }
}
