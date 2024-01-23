package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.dao.LikeDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilmService {
    private static final int MAX_NAME_SIZE = 200;
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private final FilmStorage filmStorage;
    private final LikeDbStorage likeDbStorage;

    public Film create(Film film) {
        if (film.getGenres() != null) {
            film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        }
        validation(film);
        Long filmId = filmStorage.addEntity(film);
        return filmStorage.getEntityById(filmId);
    }

    public Film update(Film film) {
        if (film.getGenres() != null) {
            film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        }
        validation(film);
        int updCount = filmStorage.updateEntity(film);

        if (updCount == 0) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id = %s", film.getId()));
        }

        return filmStorage.getEntityById(film.getId());
    }

    public Film getFilmById(Long id) {
        Film film;

        try {
            film = filmStorage.getEntityById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id = %s", id));
        }

        return film;
    }

    public List<Film> getAll() {
        return filmStorage.getAllEntities();
    }

    public void likeIt(Long userId, Long filmId) {
        likeDbStorage.addLikeFromUser(filmId, userId);
    }

    public void removeLikeIt(Long userId, Long filmId) {
        if (likeDbStorage.removeLikeFromUser(filmId, userId) == 0) {
            throw new LikeNotFoundException(String.format("Не найдена запись filmId %s userId %s", filmId, userId));
        }
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllEntities().stream()
                .sorted(Comparator.comparingLong(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toList());
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