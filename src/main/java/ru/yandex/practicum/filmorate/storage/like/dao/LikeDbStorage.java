package ru.yandex.practicum.filmorate.storage.like.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Primary
@AllArgsConstructor
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public void addLikeFromUser(Long filmId, Long userId) {
        jdbcTemplate.update("INSERT INTO USER_FILM_LIKE (USER_ID, FILM_ID) " + "VALUES (?, ?)",
                userId, filmId);
    }

    public int removeLikeFromUser(Long filmId, Long userId) {
        return jdbcTemplate.update("DELETE FROM USER_FILM_LIKE WHERE USER_ID = ? AND FILM_ID = ?",
                userId, filmId);
    }
}