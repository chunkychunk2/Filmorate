package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTest {
    InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();

    @Test
    void validateFilmNameIsBlank() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(100);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmStorage.validateFilm(film));
        assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void validateFilmDescriptionTooLong() {
        String longDescription = "a".repeat(201);
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription(longDescription);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmStorage.validateFilm(film));
        assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    void validateFilmReleaseDateBeforeMinDate() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(100);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmStorage.validateFilm(film));
        assertEquals("дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void validateFilmReleaseDateIsMinDate() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);

        assertDoesNotThrow(() -> filmStorage.validateFilm(film));
    }

    @Test
    void validateFilmDurationIsNegative() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-100);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmStorage.validateFilm(film));
        assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    @Test
    void validateFilmDurationIsZero() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmStorage.validateFilm(film));
        assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    @Test
    void validateFilmIsValid() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        assertDoesNotThrow(() -> filmStorage.validateFilm(film));
    }
}
