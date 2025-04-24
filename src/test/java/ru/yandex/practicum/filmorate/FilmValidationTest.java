package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTest extends BaseValidationTest {

    @Test
    void validateFilmNameIsBlank() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        film.setDuration(100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("Название не может быть пустым", violation.getMessage());
    }

    @Test
    void validateFilmDescriptionTooLong() {
        String longDescription = "a".repeat(201);
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription(longDescription);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("description", violation.getPropertyPath().toString());
        assertEquals("Максимальная длина описания — 200 символов", violation.getMessage());
    }

    @Test
    void validateFilmReleaseDateBeforeMinDate() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("releaseDate", violation.getPropertyPath().toString());
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", violation.getMessage());
    }

    @Test
    void validateFilmReleaseDateIsMinDate() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());
    }

    @Test
    void validateFilmDurationIsNegative() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("duration", violation.getPropertyPath().toString());
        assertEquals("Продолжительность фильма должна быть положительным числом.", violation.getMessage());
    }

    @Test
    void validateFilmDurationIsZero() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("duration", violation.getPropertyPath().toString());
        assertEquals("Продолжительность фильма должна быть положительным числом.", violation.getMessage());
    }

    @Test
    void validateFilmIsValid() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());
    }

}
