package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User addEntity(User user);

    User updateEntity(User user);

    User getEntityById(Long id);

    void deleteEntityById(Long id);

    List<User> getAllEntities();

    List<User> getEntitiesBySet(Set<Long> usersIds);
}