package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film implements Entity {
    private Long id;
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    @PositiveOrZero
    private int duration;
    private final Set<Long> likes = new HashSet<>();

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);
    }

    public int getLikesCount() {
        return likes.size();
    }
}