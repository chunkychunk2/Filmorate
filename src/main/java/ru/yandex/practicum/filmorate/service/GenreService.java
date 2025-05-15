package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDbStorage genreDbStorage;

    public Genre getGenreById(Integer genreId) {
        return genreDbStorage.getGenreById(genreId);
    }

    public Set<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

}