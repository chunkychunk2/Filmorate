package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film addLike(long filmId, long userId) {
        log.info("Доабвление <Like> к фильму");
        if (filmId <= 0 || userId <= 0) {
            throw new ValidationException("id пользователя не может быть меньше значния <1>");
        }

        Film film = filmStorage.findById(filmId);

        if (film.getLikes().contains(userId)) {
            log.warn("Пользователь c id = " + userId + " уже ставил <LIKE> фильму с id = " + filmId);
            return film;
        }
        User user = userStorage.findById(userId);
        filmStorage.addLike(film, userId);
        log.info("Пользователь {} поставил <Like> фильму {} ", user.getLogin(), film.getName());

        return film;
    }

    public void deleteLike(long filmId, long userId) {
        log.info("Удаление <Like> пользователя из фильма");
        if (filmId <= 0 || userId <= 0) {
            throw new ValidationException("id пользователя не может быть меньше значния <1>");
        }
        Film film = filmStorage.findById(filmId);

        if (!film.getLikes().contains(userId)) {
            log.warn("Пользователь c id = " + userId + " не ставил <LIKE> фильму с id = " + filmId);
        } else {
            filmStorage.removeLike(film, userId);
            log.info("<Like> удален");
        }
    }

    public Collection<Film> getTopFilms(long count) {
        log.debug("popular films count {}", count);
        return findAll().stream()
                .sorted(Comparator.comparing(Film::popularity).reversed())
                .limit(count)
                .toList();
    }
}
