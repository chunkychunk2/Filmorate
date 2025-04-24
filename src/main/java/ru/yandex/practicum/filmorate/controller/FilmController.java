package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
@RequestMapping("/films")
public class FilmController {


    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        if (film == null) {
            throw new NotFoundException("Не указан фильм для создания");
        }
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (film == null) {
            throw new NotFoundException("Не указан фильм для обновления");
        }
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") int id,
                        @PathVariable("userId") int userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int id,
                           @PathVariable("userId") int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getTopFilms(@Valid @Positive @RequestParam(defaultValue = "10") Long count) {

        return filmService.getTopFilms(count);
    }

}