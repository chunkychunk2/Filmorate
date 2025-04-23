package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    private long id;

    @Override
    public Collection<Film> findAll() {
        log.info("Получен запрос на получение списка всех фильмов.");
        return films.values();
    }

    @Override
    public Film create(Film film) {
        log.info("Получен запрос на добавление фильма: {}", film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        log.info("Получен запрос на обновление фильма: {}", newFilm);
        Long newFildId = newFilm.getId();
        if (newFildId == null) {
            log.warn("Id должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        Film oldFilm = films.get(newFildId);
        if (oldFilm == null) {
            log.warn("Фильм с id = " + newFildId + " не найден");
            throw new NotFoundException("Фильм с id = " + newFildId + " не найден");
        }
        films.put(newFildId, newFilm);
        log.info("Обновлен фильм: {}", oldFilm);
        return oldFilm;
    }

    @Override
    public Film findById(long id) {
        log.info("Получен запрос на получение фильма по id: {}", id);
        log.debug("Поиск фильма с id = <{}>", id);
        if (id <= 0) {
            throw new ValidationException("id фильма не может быть меньше значения <1>");
        }
        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        return film;
    }

    @Override
    public void addLike(Film film, long id) {
        film.getLikes().add(id);
    }

    @Override
    public void removeLike(Film film, long id) {
        film.getLikes().remove(id);
    }
}