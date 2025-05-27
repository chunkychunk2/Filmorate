package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaDbStorage mpaDbStorage;

    public Mpa getMpaById(Integer mpaId) {
        return mpaDbStorage.getMpaById(mpaId);
    }

    public Set<Mpa> getAllMpa() {
        return mpaDbStorage.getAllMpa();
    }

}
