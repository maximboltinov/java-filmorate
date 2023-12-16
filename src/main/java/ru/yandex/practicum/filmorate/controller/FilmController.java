package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final int MAX_NAME_SIZE = 200;
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Пришел запрос GET /films");
        final List<Film> filmsList = filmService.getAll();
        log.info("Отправлен ответ GET /films {}", filmsList);
        return filmsList;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Пришел запрос POST /films");

        try {
            validation(film);
        } catch (ValidationException e) {
            log.info("Не прошел валидацию с ошибкой: " + e.getMessage());
            throw e;
        }

        final Film filmResponse = filmService.create(film);
        log.info("Отправлен ответ POST /films {}", filmResponse);
        return filmResponse;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Пришел запрос PUT /films");

        try {
            validation(film);
        } catch (ValidationException e) {
            log.info("Не прошел валидацию с ошибкой: " + e.getMessage());
            throw e;
        }

        final Film filmResponse = filmService.update(film);
        log.info("Отправлен ответ PUT /films {}", filmResponse);
        return filmResponse;
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeIt(@PathVariable Long id, @PathVariable Long userId) {
        filmService.likeIt(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLikeIt(userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

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