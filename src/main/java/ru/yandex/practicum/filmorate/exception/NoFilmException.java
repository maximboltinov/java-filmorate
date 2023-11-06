package ru.yandex.practicum.filmorate.exception;

public class NoFilmException extends RuntimeException{
    public NoFilmException(String message) {
        super(message);
    }
}
