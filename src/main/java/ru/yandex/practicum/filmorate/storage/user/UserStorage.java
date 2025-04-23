package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {

    User create(User user);

    User update(User user);

    Collection<User> findAll();

    Collection<User> findUsersByIds(Set<Long> ids);

    User findById(long id);

    void addFriend(User user, long friendId);

    void removeFriend(User user, long friendId);
}
