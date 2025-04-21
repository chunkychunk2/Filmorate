package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private Long id;

    private String login;

    private String email;

    private String name;

    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();
}
