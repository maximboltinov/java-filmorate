package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User create(User user) {

        validation(user);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Пользователь без имени, имя = логин");
        }

        return userStorage.addEntity(user);
    }

    public User getUserById(Long userId) {
        final User user = userStorage.getEntityById(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", userId));
        }
        return user;
    }

    public List<User> getAll() {
        return userStorage.getAllEntities();
    }

    public void addFriend(Long userId, Long friendId) {
        try {
            userStorage.addFriend(userId, friendId);
        } catch (DataIntegrityViolationException e) {
            throw new UserNotFoundException("Не найден id");
        }
    }

    public void removeFromFriends(Long user1Id, Long user2Id) {
        userStorage.removeFromFriends(user1Id, user2Id);
    }

    public List<User> getAllFriends(Long userId) {
        return userStorage.getAllFriends(userId);
    }

    public List<User> getMutualFriends(Long user1Id, Long user2Id) {
        return userStorage.getMutualFriends(user1Id, user2Id);
    }

    public User update(User user) {
        validation(user);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Пользователь без имени, имя = логин");
        }

        return userStorage.updateEntity(user);
    }

    protected void validation(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный email");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Некорректный логин");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Некорректная дата рождения");
        }
    }
}