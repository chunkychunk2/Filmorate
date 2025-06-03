package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
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

    public void addFriend(long id, long newFriendId) {
        userStorage.addFriend(id, newFriendId);
    }

    public Optional<User> getUserById(long userId) {
        return Optional.ofNullable(userStorage.findById(userId));
    }

    public Set<User> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    public void deleteFromFriends(long id, long userToDeleteId) {
        userStorage.removeFriend(id, userToDeleteId);
    }

    public void deleteAllFriends(long userId) {
        userStorage.removeAllFriends(userId);
    }

    public Collection<User> getCommonFriends(int userId, int friendId) {
        return userStorage.getCommonFriends(userId, friendId);
    }

    public boolean friendshipVerification(long userFrom, long userTo) {
        return userStorage.friendshipVerification(userFrom, userTo);
    }

}

