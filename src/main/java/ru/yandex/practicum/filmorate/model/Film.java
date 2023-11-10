package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.Month;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    @PositiveOrZero
    private int duration;
}
