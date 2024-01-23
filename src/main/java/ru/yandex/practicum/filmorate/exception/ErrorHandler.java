package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(final ValidationException e) {
        log.info("Не прошел валидацию с ошибкой: " + e.getMessage());
        return Map.of("ошибка валидации", e.getMessage());
    }

    @ExceptionHandler(FilmNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleFilmNotFound(final FilmNotFoundException e) {
        log.info("Завершен с ошибкой: " + e.getMessage());
        return Map.of("фильм", e.getMessage());
    }

    @ExceptionHandler(FilmAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleFilmAlreadyExists(final FilmAlreadyExistsException e) {
        log.info("Завершен с ошибкой: " + e.getMessage());
        return Map.of("фильм", e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserNotFound(final UserNotFoundException e) {
        log.info("Завершен с ошибкой: " + e.getMessage());
        return Map.of("пользователь", e.getMessage());
    }

    @ExceptionHandler(LikeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleLikeNotFound(final LikeNotFoundException e) {
        log.info("Завершен с ошибкой: " + e.getMessage());
        return Map.of("лайк", e.getMessage());
    }

    @ExceptionHandler(RatingMpaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleMpaNotFound(final RatingMpaNotFoundException e) {
        log.info("Завершен с ошибкой: " + e.getMessage());
        return Map.of("рейтинг", e.getMessage());
    }

    @ExceptionHandler(GenreNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleGenreNotFound(final GenreNotFoundException e) {
        log.info("Завершен с ошибкой: " + e.getMessage());
        return Map.of("жанр", e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUserAlreadyExists(final UserAlreadyExistsException e) {
        log.info("Завершен с ошибкой: " + e.getMessage());
        return Map.of("пользователь", e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleRuntimeException(final RuntimeException e) {
        log.info("Завершен с ошибкой: " + e.getMessage());
        return Map.of("пользователь", e.getMessage());
    }
}