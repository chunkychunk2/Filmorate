package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class MpaController {

    @Autowired
    private final MpaService mpaService;

    @GetMapping("/mpa/{id}")
    public Mpa findMpaById(@PathVariable Integer id) {
        return mpaService.getMpaById(id);
    }

    @GetMapping("/mpa")
    public Set<Mpa> findAllMpa() {
        return mpaService.getAllMpa();
    }

}
