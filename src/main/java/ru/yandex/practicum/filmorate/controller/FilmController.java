package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final int MAX_NAME_SIZE = 200;
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int idCounter = 1;

    @GetMapping
    public List<Film> getAll() {
        log.info("Пришел запрос GET /films");
        final List<Film> filmsList = new ArrayList<>(films.values());
        log.info("Отправлен ответ GET /films {}", filmsList);
        return filmsList;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Пришел запрос POST /films");

        if (films.containsKey(film.getId())) {
            ValidationException e = new ValidationException("Такой фильм уже существует");
            log.debug("Запрос завершен ошибкой: " + e.getMessage());
            throw e;
        }

        try {
            filmValidation(film);
        } catch (ValidationException e) {
            log.info("Не прошел валидацию с ошибкой: " + e.getMessage());
            throw e;
        }

        final int id = idCounter++;
        film.setId(id);
        films.put(id, film);
        log.info("Отправлен ответ POST /films {}", films.get(id));
        return films.get(id);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Пришел запрос PUT /films");

        final int id = film.getId();
        if (!films.containsKey(id)) {
            ValidationException e = new ValidationException("Фильм не найден");
            log.debug("Запрос завершен ошибкой: " + e.getMessage());
            throw e;
        }

        try {
            filmValidation(film);
        } catch (ValidationException e) {
            log.info("Не прошел валидацию с ошибкой: " + e.getMessage());
            throw e;
        }

        films.put(id, film);
        log.info("Отправлен ответ PUT /films {}", films.get(id));
        return films.get(id);
    }

    private void filmValidation(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("нет названия фильма");
        }
        if (film.getDescription().length() > MAX_NAME_SIZE) {
            throw new ValidationException("длина описания более " + MAX_NAME_SIZE + " символов");
        }
        if (film.getReleaseDate().isBefore(FILM_BIRTHDAY)) {
            throw new ValidationException("слишком ранняя дата релиза");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("отрицательная продолжительность");
        }
    }
}