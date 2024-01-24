package ru.yandex.practicum.filmorate.storage.mpa.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@AllArgsConstructor
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public RatingMPA getMpaById(Long mpaId) {
        return jdbcTemplate.queryForObject("SELECT ID MPA_ID, NAME MPA_NAME " +
                        "FROM MPA WHERE ID = ?",
                this::mapToMpa, mpaId);
    }

    public List<RatingMPA> getAllMpa() {
        return jdbcTemplate.query("SELECT ID MPA_ID, NAME MPA_NAME " +
                        "FROM MPA",
                this::mapToMpa);
    }

    public RatingMPA mapToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        RatingMPA ratingMPA = new RatingMPA();
        ratingMPA.setId(resultSet.getLong("MPA_ID"));
        ratingMPA.setName(resultSet.getString("MPA_NAME"));

        return ratingMPA;
    }
}