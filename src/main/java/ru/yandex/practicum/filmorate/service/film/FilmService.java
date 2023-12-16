package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utility.IdGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private final IdGenerator idGenerator;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       UserStorage userStorage,
                       @Qualifier("idGeneratorForFilms") IdGenerator idGenerator) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.idGenerator = idGenerator;
    }

    public void likeIt(Long userId, Long filmId) {

        if (!userStorage.isEntityRegistered(userId)) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", userId));
        }

        if (filmStorage.getEntityById(filmId) == null) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id = %s", filmId));
        }

        Film film = filmStorage.getEntityById(filmId);
        film.addLike(userId);
        filmStorage.addEntity(film);
    }

    public void removeLikeIt(Long userId, Long filmId) {

        if (!userStorage.isEntityRegistered(userId)) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", userId));
        }

        if (filmStorage.getEntityById(filmId) == null) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id = %s", filmId));
        }

        Film film = filmStorage.getEntityById(filmId);
        film.removeLike(userId);
        filmStorage.addEntity(film);
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
        if (filmStorage.isEntityRegistered(film.getId())) {
            throw new FilmAlreadyExistsException(String.format("Фильм с id = %s уже существует", film.getId()));
        }

        Long id = idGenerator.generateId();
        log.info(String.format("создан фильм с id %s", id));
        film.setId(id);
        return filmStorage.addEntity(film);
    }

    public Film update(Film film) {
        if (filmStorage.getEntityById(film.getId()) == null) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id = %s", film.getId()));
        }

        return filmStorage.addEntity(film);
    }

    public boolean isFilmRegistered(Long id) {
        return filmStorage.isEntityRegistered(id);
    }
}