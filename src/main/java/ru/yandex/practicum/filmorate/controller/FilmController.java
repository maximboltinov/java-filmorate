package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NoFilmException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utility.LocalDateAdapter;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static int idCounter = 1;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe())
            .create();

    @GetMapping
    public String findAll() {
        return gson.toJson(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        String message = filmValidation(film);
        if (!message.isEmpty()) {
            log.debug("Создание: не прошел валидацию: " + message);
            throw new ValidationException(message);
        }
        if (films.containsKey(film.getId())) {
            log.debug("Создание: повтор фильма");
            throw new UserAlreadyExistException("Такой фильм уже существует");
        }
        final int id = idCounter++;
        film.setId(id);
        films.put(id, film);
        log.info("Создание: добавлен фильм с id: " + id);
        return films.get(id);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        String message = filmValidation(film);
        if (!message.isEmpty()) {
            log.debug("Обновление: не прошел валидацию: " + message);
            throw new ValidationException(message);
        }
        int id = film.getId();
        if (!films.containsKey(id)) {
            log.debug("Обновление: фильм отсутствует");
            throw new NoFilmException("такого фильма не существует");
        }
        films.put(id, film);
        log.info("Обновление: обновлен фильм с id: " + id);
        return films.get(id);
    }

    private String filmValidation(Film film) {
        StringBuilder stringBuilder = new StringBuilder();
        if (film.getName().isBlank()) {
            stringBuilder.append("нет названия");
        }
        if (film.getDescription().length() > 200) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(" & ");
            }
            stringBuilder.append("длина описания более 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(" & ");
            }
            stringBuilder.append("слишком ранняя дата релиза");
        }
        if (film.getDuration() < 0) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(" & ");
            }
            stringBuilder.append("отрицательная продолжительность");
        }
        return stringBuilder.toString();
    }
}