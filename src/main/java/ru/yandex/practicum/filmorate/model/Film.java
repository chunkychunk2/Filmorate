package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Film {

    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не может быть null")
    @ReleaseDate
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом.")
    private Integer duration;

    @Builder.Default
    private Set<Genre> genres = new HashSet<>();
    private Mpa mpa;

    public List<Genre> getGenres() {
        Set<Genre> uniqueGenres = new HashSet<>(genres);
        return new ArrayList<>(uniqueGenres);
    }

    @Builder.Default
    private Set<Long> likes = new HashSet<>();

    public int popularity() {
        return likes.size();
    }

}
