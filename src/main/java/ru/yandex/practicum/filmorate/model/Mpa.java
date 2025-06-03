package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
public class Mpa {

    private int id;

    private String name;

    public Mpa(int mpaId, String name) {
        this.id = mpaId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mpa mpa = (Mpa) o;
        return id == mpa.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
