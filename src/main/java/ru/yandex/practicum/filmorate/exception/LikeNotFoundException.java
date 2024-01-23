package ru.yandex.practicum.filmorate.exception;

public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException(String m) {
        super(m);
    }
}