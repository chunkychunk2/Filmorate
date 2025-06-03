package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;


@RestController
@ResponseBody
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        if (user == null) {
            throw new NotFoundException("Не указан пользователь для создания");
        }
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (user == null) {
            throw new NotFoundException("Не указан пользователь для обновления");
        }
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long id,
                          @PathVariable("friendId") Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable("id") int id,
                                  @PathVariable("friendId") int friendId) {
        userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") int id,
                                             @PathVariable("otherId") int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUsersFriends(@PathVariable("id") long id) {
        return userService.getFriends(id);
    }


    @GetMapping("/{id}/friends/{otherId}")
    public boolean friendshipVerification(long id, long otherId) {
        return userService.friendshipVerification(id, otherId);
    }

}
