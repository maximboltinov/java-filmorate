package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilmService {
    private static final int MAX_NAME_SIZE = 200;
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private final FilmStorage filmStorage;
    private final GenreService genreService;

    public Film addFilm(Film film) {
        if (film.getGenres() != null) {
            film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        }
        validation(film);
        Long filmId = filmStorage.addFilm(film);
        return getFilmById(filmId);
    }

    public Film updateFilm(Film film) {
        if (film.getGenres() != null) {
            film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        }
        validation(film);
        int updCount = filmStorage.updateFilm(film);

        if (updCount == 0) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id = %s", film.getId()));
        }

        return getFilmById(film.getId());
    }

    public Film getFilmById(Long id) {
        Film film;

        try {
            film = filmStorage.getFilmById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id = %s", id));
        }

        List<Genre> genres = genreService.getGenresByFilmId(id);
        film.setGenres(genres);

        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        Map<Long, List<Genre>> result = genreService.getGenresForAllFilms();

        for (Film film : films) {
            film.setGenres(result.get(film.getId()));
            if (film.getGenres() == null) {
                film.setGenres(new ArrayList<>());
            }
        }

        return films;
    }

    public List<Film> getPopularFilms(int count) {
        return getAllFilms().stream()
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