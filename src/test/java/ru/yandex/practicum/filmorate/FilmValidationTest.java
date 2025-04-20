package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTest {
    FilmController filmController = new FilmController();

    @Test
    void validateFilmNameIsBlank() {
        Film film = Film.builder()
                .name("")
                .description("Valid description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
        assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void validateFilmDescriptionTooLong() {
        String longDescription = "a".repeat(201);
        Film film = Film.builder()
                .name("Valid Name")
                .description(longDescription)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
        assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    void validateFilmReleaseDateBeforeMinDate() {
        Film film = Film.builder()
                .name("Valid Name")
                .description("Valid Description")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(100)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
        assertEquals("дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void validateFilmReleaseDateIsMinDate() {
        Film film = Film.builder()
                .name("Valid Name")
                .description("Valid Description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .build();

        assertDoesNotThrow(() -> filmController.validateFilm(film));
    }

    @Test
    void validateFilmDurationIsNegative() {
        Film film = Film.builder()
                .name("Valid Name")
                .description("Valid Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(-100)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
        assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    @Test
    void validateFilmDurationIsZero() {
        Film film = Film.builder()
                .name("Valid Name")
                .description("Valid Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(0)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
        assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
    }

    @Test
    void validateFilmIsValid() {
        Film film = Film.builder()
                .name("Valid Name")
                .description("Valid Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        assertDoesNotThrow(() -> filmController.validateFilm(film));
    }
}
