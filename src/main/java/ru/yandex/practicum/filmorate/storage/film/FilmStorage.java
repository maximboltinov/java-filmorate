package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addEntity(Film film);

    Film updateEntity(Film film);

    Film getEntityById(Long id);

    void deleteEntityById(Long id);

    List<Film> getAllEntities();
}