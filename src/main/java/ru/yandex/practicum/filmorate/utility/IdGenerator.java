package ru.yandex.practicum.filmorate.utility;

public class IdGenerator {
    private Long counter = 1L;

    public Long generateId() {
        return counter++;
    }
}