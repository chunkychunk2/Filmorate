package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    Collection<Film> findAll();

    Film findById(long id);

    void addLike(Film film, long id);

    void removeLike(Film film, long id);

}
