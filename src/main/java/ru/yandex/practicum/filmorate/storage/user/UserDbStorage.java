package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
@Primary
@Qualifier("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            user.setId(keyHolder.getKey().longValue());
        }
        return user;
    }

    @Override
    public User findById(long userId) {
        String sql = "SELECT * " +
                "FROM users " +
                "WHERE user_id = ?";
        User user = jdbcTemplate.query(sql, this::mapRowToUser, userId).stream()
                .findFirst()
                .orElse(null);
        return user;
    }

    @Override
    public User findByName(String name) {
        String sql = "SELECT * " +
                "FROM users " +
                "WHERE name = ?";
        User user = jdbcTemplate.query(sql, this::mapRowToUser, name).stream()
                .findFirst()
                .orElse(null);
        return user;
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getLong("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getDate("birthday").toLocalDate(),
                rs.getString("name")
        );
    }

    @Override
    public User update(User user) {
        if (!userExists(user.getId())) {
            throw new NotFoundException("User with ID = " + user.getId() + " is not found");
        }
        String sql = "UPDATE users " +
                "SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return findById(user.getId());
    }

    @Override
    public void addFriend(long id, long friendId) {
        if (findById(friendId) == null || friendId == 0) {
            throw new NotFoundException("User with id = " + friendId + " is not found");
        }
        String sql = "INSERT INTO friendship (user_id, friend_id) " +
                "VALUES (?, ?);";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public Set<User> getFriends(Long userId) {
        if (findById(userId) == null || userId == 0) {
            throw new NotFoundException("User with id = " + userId + " is not found");
        }
        String sql = "SELECT friend_id " +
                "FROM friendship " +
                "WHERE user_id = ?";
        Set<Long> friendIds = new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("friend_id"), userId));
        Set<User> friends = new HashSet<>();
        for (Long friendId : friendIds) {
            User friend = findById(friendId);
            if (friend != null) {
                friends.add(friend);
            }
        }
        return friends;
    }

    @Override
    public void removeFriend(long id, long friendId) {
        if (findById(friendId) == null || friendId == 0) {
            throw new NotFoundException("User with id = " + friendId + " is not found");
        }
        String sql = "DELETE FROM friendship " +
                "WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void removeAllFriends(long userId) {
        String sql = "DELETE FROM friendship " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public Set<User> getCommonFriends(long user1Id, long user2Id) {
        String sql = "SELECT u.user_id, u.email, u.login, u.name, u.birthday " +
                "FROM friendship fs " +
                "INNER JOIN friendship fr " +
                "ON fs.friend_id = fr.friend_id " +
                "INNER JOIN users u " +
                "ON u.user_id = fs.friend_id " +
                "WHERE fs.user_id = ? AND fr.user_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToUser(rs, rowNum), user1Id, user2Id));
    }

    @Override
    public boolean friendshipVerification(long user1Id, long user2Id) {
        return true;
    }

    private boolean userExists(Long userId) {
        String sql = "SELECT EXISTS(SELECT 1 " +
                "FROM users " +
                "WHERE user_id = ? " +
                "LIMIT 1)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, userId);
    }

}
