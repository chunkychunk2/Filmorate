package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.AtSymbol;
import ru.yandex.practicum.filmorate.validator.EmailUniqueness;
import ru.yandex.practicum.filmorate.validator.LoginUniqueness;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class User {

    public User(Long id, String email, String login, LocalDate birthday, String name) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.name = name;
        this.friends = new HashSet<>();
    }

    private Long id;

    @NotBlank(message = "Логин не может быть пустым")
    @NoSpaces
    @LoginUniqueness
    private String login;

    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email не может быть пустым")
    @AtSymbol
    @EmailUniqueness
    private String email;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем")
    @NotNull(message = "Дата рождения не может быть null")
    private LocalDate birthday;

    @Builder.Default
    private Set<Long> friends = new HashSet<>();

    public String getName() {
        return (name == null || name.isBlank()) ? login : name;
    }

}

