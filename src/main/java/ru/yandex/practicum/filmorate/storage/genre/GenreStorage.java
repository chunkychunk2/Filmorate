package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreStorage {

    Genre getGenreById(Integer genreId);

    Set<Genre> getAllGenres();

}
