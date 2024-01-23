package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Long addEntity(Film film);

    int updateEntity(Film film);

    Film getEntityById(Long id);

    void deleteEntityById(Long id);

    List<Film> getAllEntities();
}