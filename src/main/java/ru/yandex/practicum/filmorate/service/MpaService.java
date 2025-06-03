package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaStorage;

    public Mpa getMpaById(Integer mpaId) {
        return mpaStorage.getMpaById(mpaId);
    }

    public Set<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

}
