package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    private long id = 0;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос на получение списка всех фильмов.");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен запрос на добавление фильма: {}", film);
        validateFilm(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Получен запрос на обновление фильма: {}", newFilm);
        Long newFildId = newFilm.getId();
        if (newFildId == null) {
            log.warn("Id должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (films.containsKey(newFildId)) {
            Film oldFilm = films.get(newFildId);
            validateFilm(newFilm);
            films.put(newFildId, newFilm);
            log.info("Обновлен фильм: {}", oldFilm);
            return oldFilm;
        } else {
            log.warn("Фильм с id = " + newFildId + " не найден");
            throw new NotFoundException("Фильм с id = " + newFildId + " не найден");
        }
    }

    public void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() == null || film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть положительным числом.");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }
}