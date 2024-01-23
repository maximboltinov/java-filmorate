package ru.yandex.practicum.filmorate.storage.genre.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Primary
@AllArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Genre getGenreById(Long genreId) {
        return jdbcTemplate.queryForObject("SELECT ID GENRE_ID, NAME GENRE_NAME " +
                        "FROM GENRE WHERE ID = ?",
                this::mapToGenre, genreId);
    }

    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT ID GENRE_ID, NAME GENRE_NAME FROM GENRE", this::mapToGenre);
    }

    public List<Genre> getGenresByFilmId(Long filmId) {
        return jdbcTemplate.query("SELECT FG.GENRE_ID, G.NAME GENRE_NAME " +
                        "FROM FILM_GENRE FG " +
                        "JOIN GENRE G ON FG.GENRE_ID = G.ID WHERE FG.FILM_ID = ?",
                this::mapToGenre, filmId);
    }

    public Map<Long, List<Genre>> getGenresForAllFilms() {
        List<FilmGenre> filmsGenresFetch = jdbcTemplate.query(
                "SELECT FG.FILM_ID, FG.GENRE_ID, G.NAME GENRE_NAME " +
                        "FROM FILM_GENRE FG JOIN GENRE G ON FG.GENRE_ID = G.ID",
                this::mapToFilmGenre);

        Map<Long, List<Genre>> result = new HashMap<>();
        filmsGenresFetch.forEach(element -> result.computeIfAbsent(element.getFilmId(),
                x -> new ArrayList<>()).add(element.getGenre()));
        return result;
    }

    private Genre mapToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getLong("GENRE_ID"));
        genre.setName(resultSet.getString("GENRE_NAME"));

        return genre;
    }

    @Data
    private static class FilmGenre {
        private Long filmId;
        private Genre genre;
    }

    private FilmGenre mapToFilmGenre(ResultSet resultSet, int rowNum) throws SQLException {
        FilmGenre filmGenre = new FilmGenre();
        filmGenre.setFilmId(resultSet.getLong("FILM_ID"));
        filmGenre.setGenre(mapToGenre(resultSet, rowNum));

        return filmGenre;
    }
}