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
        validation(film);
        final Film filmResponse = filmService.create(film);
        log.info("Отправлен ответ POST /films {}", filmResponse);
        return filmResponse;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Пришел запрос PUT /films");
        validation(film);
        final Film filmResponse = filmService.update(film);
        log.info("Отправлен ответ PUT /films {}", filmResponse);
        return filmResponse;
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeIt(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пришел запрос PUT /films/{}/like/{}", id, userId);
        filmService.likeIt(userId, id);
        log.info("Отправлен ответ PUT /films/{}/like/{}", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пришел запрос DELETE /films/{}/like/{}", id, userId);
        filmService.removeLikeIt(userId, id);
        log.info("Отправлен ответ DELETE /films/{}/like/{}", id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Пришел запрос GET /films/popular");
        final List<Film> list = filmService.getPopularFilms(count);
        log.info("Отправлен ответ GET /films/popular {}", list);
        return list;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Пришел запрос GET /films/{}", id);
        final Film film = filmService.getFilmById(id);
        log.info("Отправлен ответ GET /films/{} {}", id, film);
        return film;
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