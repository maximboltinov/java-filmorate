package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends Controller<Film> {
    private static final int MAX_NAME_SIZE = 200;
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);

    @Override
    @GetMapping
    public List<Film> getAll() {
        log.info("Пришел запрос GET /films");
        final List<Film> filmsList = super.getAll();
        log.info("Отправлен ответ GET /films {}", filmsList);
        return filmsList;
    }

    @Override
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Пришел запрос POST /films");

        if (storage.containsKey(film.getId())) {
            ValidationException e = new ValidationException("Такой фильм уже существует");
            log.debug("Запрос завершен ошибкой: " + e.getMessage());
            throw e;
        }

        try {
            validation(film);
        } catch (ValidationException e) {
            log.info("Не прошел валидацию с ошибкой: " + e.getMessage());
            throw e;
        }

        final Film filmResponse = super.create(film);
        log.info("Отправлен ответ POST /films {}", filmResponse);
        return filmResponse;
    }

    @Override
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Пришел запрос PUT /films");

        final int id = film.getId();
        if (!storage.containsKey(id)) {
            ValidationException e = new ValidationException("Фильм не найден");
            log.debug("Запрос завершен ошибкой: " + e.getMessage());
            throw e;
        }

        try {
            validation(film);
        } catch (ValidationException e) {
            log.info("Не прошел валидацию с ошибкой: " + e.getMessage());
            throw e;
        }

        final Film filmResponse = super.update(film);
        log.info("Отправлен ответ PUT /films {}", filmResponse);
        return filmResponse;
    }

    @Override
    protected void validation(Film film) {
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