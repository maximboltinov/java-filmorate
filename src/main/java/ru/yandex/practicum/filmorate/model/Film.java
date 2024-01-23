package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class Film {
    private Long id;
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    @PositiveOrZero
    private Integer duration;
    private Integer rate;
    private RatingMPA mpa;
    private List<Genre> genres;

    public Map<String, Object> toMapForDB() {
        return Map.of("name", name, "description", description,
                "release_date", java.sql.Date.valueOf(releaseDate),
                "duration", duration, "rating_id", mpa.getId());
    }
}