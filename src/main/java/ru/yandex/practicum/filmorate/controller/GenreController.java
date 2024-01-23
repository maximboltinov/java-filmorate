package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> getAllGenres() {
        log.info("Получен запрос GET /genres");
        List<Genre> genres = genreService.getAllGenres();
        log.info("Отправлен ответ GET /genres {}", genres);
        return genres;
    }

    @GetMapping("/{genreId}")
    public Genre getGenreById(@PathVariable Long genreId) {
        log.info("Получен запрос GET /genres/{}", genreId);
        Genre genre = genreService.getGenreById(genreId);
        log.info("Отправлен ответ GET /genres/{} {}", genreId, genre);
        return genre;
    }
}