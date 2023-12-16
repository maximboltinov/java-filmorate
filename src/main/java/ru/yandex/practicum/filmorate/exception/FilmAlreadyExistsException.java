package ru.yandex.practicum.filmorate.exception;

public class FilmAlreadyExistsException extends RuntimeException {
    public FilmAlreadyExistsException(String massage) {
        super(massage);
    }
}