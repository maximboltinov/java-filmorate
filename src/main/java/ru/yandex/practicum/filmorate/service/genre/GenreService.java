package ru.yandex.practicum.filmorate.service.genre;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.dao.GenreDbStorage;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public Genre getGenreById(Long genreId) {
        Genre genre;
        try {
            genre = genreDbStorage.getGenreById(genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundException(String.format("Не найден жанр с id = %s", genreId));
        }
        return genre;
    }

    public List<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public List<Genre> getGenresByFilmId(Long filmId) {
        return genreDbStorage.getGenresByFilmId(filmId);
    }

    public Map<Long, List<Genre>> getGenresForAllFilms() {
        return genreDbStorage.getGenresForAllFilms();
    }
}