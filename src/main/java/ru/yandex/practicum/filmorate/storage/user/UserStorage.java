package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {

    User create(User user);

    User update(User user);

    Collection<User> findAll();

    User findById(long id);

    User findByName(String name);

    void addFriend(long id, long friendId);

    void removeFriend(long id, long friendId);

    void removeAllFriends(long userId);

    Set<User> getFriends(Long userId);

    Set<User> getCommonFriends(long user1Id, long user2Id);

    boolean friendshipVerification(long user1Id, long user2Id);

}

