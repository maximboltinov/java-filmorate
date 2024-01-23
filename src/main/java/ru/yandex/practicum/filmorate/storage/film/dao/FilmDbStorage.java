package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


@Component
@Primary
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreService genreService;

    @Override
    public Long addEntity(Film film) {
        SimpleJdbcInsert simpleJdbcInsert =
                new SimpleJdbcInsert(Objects.requireNonNull(jdbcTemplate.getDataSource()))
                        .withTableName("films").usingGeneratedKeyColumns("id");

        Long filmId = simpleJdbcInsert.executeAndReturnKey(film.toMapForDB()).longValue();

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) " +
                        "VALUES (?, ?)", filmId, genre.getId());
            }
        }

        return filmId;
    }

    @Override
    public int updateEntity(Film film) {
        int updCount = jdbcTemplate.update("UPDATE FILMS " +
                        "SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, " +
                        "DURATION = ?, RATING_ID = ? WHERE ID = ?",
                film.getName(), film.getDescription(), java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getMpa().getId(), film.getId());

        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID = ?", film.getId());

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)",
                        film.getId(), genre.getId());
            }
        }

        return updCount;
    }

    @Override
    public Film getEntityById(Long id) {
        Film film = jdbcTemplate.queryForObject("SELECT F.*, RM.NAME RM_NAME, COUNT(UFL.USER_ID) RATE " +
                "FROM films F " +
                "JOIN RATING_MPA RM on F.RATING_ID = RM.ID " +
                "LEFT JOIN USER_FILM_LIKE UFL ON F.ID = UFL.FILM_ID " +
                "WHERE F.ID = ? " + "GROUP BY F.ID", this::mapToFilm, id);

        List<Genre> genres = genreService.getGenresByFilmId(id);

        if (film != null) {
            film.setGenres(genres);
        }

        return film;
    }

    @Override
    public List<Film> getAllEntities() {
        Map<Long, List<Genre>> result = genreService.getGenresForAllFilms();

        List<Film> films = jdbcTemplate.queryForStream(
                "SELECT F.*, RM.NAME RM_NAME, COUNT(UFL.USER_ID) RATE " +
                        "FROM films F " + "JOIN RATING_MPA RM on F.RATING_ID = RM.ID " +
                        "LEFT JOIN USER_FILM_LIKE UFL ON F.ID = UFL.FILM_ID " +
                        "GROUP BY F.ID",
                this::mapToFilm).sorted(Comparator.comparing(Film::getId)).collect(Collectors.toList());

        for (Film film : films) {
            film.setGenres(result.get(film.getId()));
            if (film.getGenres() == null) {
                film.setGenres(new ArrayList<>());
            }
        }

        return films;
    }

    @Override
    public void deleteEntityById(Long id) {
    }


    private Film mapToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        RatingMPA ratingMPA = new RatingMPA();
        ratingMPA.setId(resultSet.getLong("rating_id"));
        ratingMPA.setName(resultSet.getString("RM_NAME"));

        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .mpa(ratingMPA).build();
    }
}