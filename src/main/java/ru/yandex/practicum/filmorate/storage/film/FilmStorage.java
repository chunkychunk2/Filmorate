package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    Collection<Film> findAll();

    Film findById(long id);

    Film findByName(String name);

    Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException;

    Set<Long> getLikes(long filmId);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Film> getPopularFilms(Integer count);
}

