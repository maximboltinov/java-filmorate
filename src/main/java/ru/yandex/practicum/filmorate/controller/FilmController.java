package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Пришел запрос POST /films");
        final Film filmResponse = filmService.create(film);
        log.info("Отправлен ответ POST /films {}", filmResponse);
        return filmResponse;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Пришел запрос PUT /films");
        final Film filmResponse = filmService.update(film);
        log.info("Отправлен ответ PUT /films {}", filmResponse);
        return filmResponse;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Пришел запрос GET /films/{}", id);
        final Film film = filmService.getFilmById(id);
        log.info("Отправлен ответ GET /films/{} {}", id, film);
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Пришел запрос GET /films");
        final List<Film> filmsList = filmService.getAll();
        log.info("Отправлен ответ GET /films {}", filmsList);
        return filmsList;
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
}