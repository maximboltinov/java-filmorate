package ru.yandex.practicum.filmorate.service.mpa;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.RatingMpaNotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.mpa.dao.MpaDbStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    public RatingMPA getMpaById(Long mpaId) {
        RatingMPA ratingMPA;

        try {
            ratingMPA = mpaDbStorage.getMpaById(mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new RatingMpaNotFoundException(String.format("Не найден рейтинг с id = %s", mpaId));
        }

        return ratingMPA;
    }

    public List<RatingMPA> getAllMpa() {
        return mpaDbStorage.getAllMpa();
    }
}