package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final int MAX_NAME_SIZE = 200;
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void likeIt(Long userId, Long filmId) {

        if (userStorage.getEntityById(userId) == null) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", userId));
        }

        Film film = filmStorage.getEntityById(filmId);
        if (film == null) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id = %s", filmId));
        }

        film.addLike(userId);
        filmStorage.updateEntity(film);
    }

    public void removeLikeIt(Long userId, Long filmId) {

        if (userStorage.getEntityById(userId) == null) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", userId));
        }

        Film film = filmStorage.getEntityById(filmId);
        if (film == null) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id = %s", filmId));
        }

        film.removeLike(userId);
        filmStorage.updateEntity(film);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllEntities().stream()
                .sorted(Comparator.comparingLong(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getAll() {
        return filmStorage.getAllEntities();
    }

    public Film getFilmById(Long id) {
        final Film film = filmStorage.getEntityById(id);
        if (film == null) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id = %s", id));
        }
        return film;
    }

    public Film create(Film film) {
        if (filmStorage.getEntityById(film.getId()) != null) {
            throw new FilmAlreadyExistsException(String.format("Фильм с id = %s уже существует", film.getId()));
        }
        validation(film);
        return filmStorage.addEntity(film);
    }

    public Film update(Film film) {
        if (filmStorage.getEntityById(film.getId()) == null) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id = %s", film.getId()));
        }
        validation(film);
        return filmStorage.updateEntity(film);
    }

    protected void validation(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("нет названия фильма");
        }
        if (film.getDescription().length() > MAX_NAME_SIZE) {
            throw new ValidationException("длина описания более " + MAX_NAME_SIZE + " символов");
        }
        if (film.getReleaseDate().isBefore(FILM_BIRTHDAY)) {
            throw new ValidationException("слишком ранняя дата релиза");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("отрицательная продолжительность");
        }
    }
}