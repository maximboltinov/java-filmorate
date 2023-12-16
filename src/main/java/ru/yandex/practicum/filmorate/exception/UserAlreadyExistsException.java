package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String massage) {
        super(massage);
    }
}