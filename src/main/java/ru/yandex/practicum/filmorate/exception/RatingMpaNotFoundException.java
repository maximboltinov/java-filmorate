package ru.yandex.practicum.filmorate.exception;

public class RatingMpaNotFoundException extends RuntimeException {
    public RatingMpaNotFoundException(String m) {
        super(m);
    }
}