package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Long addFilm(Film film);

    int updateFilm(Film film);

    Film getFilmById(Long id);

    void deleteFilmById(Long id);

    List<Film> getAllFilms();
}