package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Long counter = 1L;
    protected final Map<Long, Film> storage = new HashMap<>();

    @Override
    public Film addEntity(Film film) {
        film.setId(generateId());
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateEntity(Film film) {
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getEntityById(Long id) {
        return storage.get(id);
    }

    @Override
    public void deleteEntityById(Long id) {
        storage.remove(id);
    }

    @Override
    public List<Film> getAllEntities() {
        return new ArrayList<>(storage.values());
    }

    private Long generateId() {
        return counter++;
    }
}