package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Controller<T extends Entity> {
    protected final Map<Long, T> storage = new HashMap<>();
    private Long idCounter = 1L;

    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    public T create(T entity) {
        final Long id = idCounter++;
        entity.setId(id);
        storage.put(id, entity);
        return storage.get(id);
    }

    public T update(T entity) {
        final Long id = entity.getId();
        storage.put(id, entity);
        return storage.get(id);
    }

    abstract void validation(T entity);
}