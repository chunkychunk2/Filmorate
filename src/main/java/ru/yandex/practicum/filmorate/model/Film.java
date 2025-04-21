package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private Integer duration;

    private Set<Long> likes = new HashSet<>();

    public int popularity() {
        return likes.size();
    }
}
