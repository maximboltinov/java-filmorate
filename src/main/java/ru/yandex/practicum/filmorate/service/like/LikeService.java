package ru.yandex.practicum.filmorate.service.like;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.storage.like.dao.LikeDbStorage;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeDbStorage likeDbStorage;

    public void addLikeFromUser(Long filmId, Long userId) {
        likeDbStorage.addLikeFromUser(filmId, userId);
    }

    public void removeLikeFromUser(Long filmId, Long userId) {
        if (likeDbStorage.removeLikeFromUser(filmId, userId) == 0) {
            throw new LikeNotFoundException(String.format("Не найдена запись filmId %s userId %s", filmId, userId));
        }
    }
}