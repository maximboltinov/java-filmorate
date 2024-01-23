package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@AllArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping("/{mpaId}")
    public RatingMPA getMpaById(@PathVariable Long mpaId) {
        log.info("Получен запрос GET /mpa/{}", mpaId);
        RatingMPA ratingMPA = mpaService.getMpaById(mpaId);
        log.info("Отправлен ответ GET /mpa/{} {}", mpaId, ratingMPA);
        return ratingMPA;
    }

    @GetMapping
    public List<RatingMPA> getAllMpa() {
        log.info("Получен запрос GET /mpa");
        List<RatingMPA> ratingMPA = mpaService.getAllMpa();
        log.info("Отправлен ответ GET /mpa {}", ratingMPA);
        return ratingMPA;
    }
}