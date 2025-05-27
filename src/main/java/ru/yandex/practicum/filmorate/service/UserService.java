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
    @Autowired
    private final UserDbStorage userDbStorage;

    public User create(@Valid User user) {
        return userDbStorage.create(user);
    }

    public User update(@Valid User user) {
        return userDbStorage.update(user);
    }

    public Collection<User> findAll() {
        return userDbStorage.findAll();
    }

    public void addFriend(long id, long newFriendId) {
        userDbStorage.addFriend(id, newFriendId);
    }

    public Optional<User> getUserById(long userId) {
        return Optional.ofNullable(userDbStorage.findById(userId));
    }

    public Set<User> getFriends(Long userId) {
        return userDbStorage.getFriends(userId);
    }

    public void deleteFromFriends(long id, long userToDeleteId) {
        userDbStorage.removeFriend(id, userToDeleteId);
    }

    public void deleteAllFriends(long userId) {
        userDbStorage.removeAllFriends(userId);
    }

    public Collection<User> getCommonFriends(int userId, int friendId) {
        return userDbStorage.getCommonFriends(userId, friendId);
    }

    public boolean friendshipVerification(long user1Id, long user2Id) {
        return userDbStorage.friendshipVerification(user1Id, user2Id);
    }

}

