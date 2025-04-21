package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@Slf4j
public class FilmController {


    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
       return filmService.findAll();
    }

    @PostMapping("/films")
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@RequestBody Film film) {
        if (film == null) {
            throw new NotFoundException("Не указан фильм для создания");
        }
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        if (film == null) {
            throw new NotFoundException("Не указан фильм для обновления");
        }
        return filmService.update(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") int id,
                        @PathVariable("userId") int userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public String deleteLike(@PathVariable("id") int id,
                                 @PathVariable("userId") int userId) {
        filmService.deleteLike(id, userId);
        return "<Like> Успешно удален";
    }

    @GetMapping("/films/popular")
    public Collection<Film> getTopFilms (@RequestParam(defaultValue = "10") String count) {
        return filmService.getTopFilms(Long.parseLong(count));
    }
}