package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;


@RestController
@ResponseBody
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        if (user == null) {
            throw new NotFoundException("Не указан пользователь для создания");
        }
        return userService.create(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        if (user == null) {
            throw new NotFoundException("Не указан пользователь для обновления");
        }
        return userService.update(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") int id,
                          @PathVariable("friendId") int friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("users/{id}/friends/{friendId}")
    public User deleteFromFriends(@PathVariable("id") int id,
                                  @PathVariable("friendId") int friendId) {
        return userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable("id") int id,
                                             @PathVariable("otherId") int otherId) {
        return userService.getMutualFriends(id,otherId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getUsersFriends(@PathVariable("id") int id) {
        return userService.getUsersFriends(id);
    }
}