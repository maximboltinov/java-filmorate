package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(Long user1Id, Long user2Id) {
        final User user1 = userStorage.getEntityById(user1Id);
        if (user1 == null) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", user1Id));
        }

        final User user2 = userStorage.getEntityById(user2Id);
        if (user2 == null) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", user2Id));
        }

        if (Objects.equals(user1Id, user2Id)) {
            throw new ValidationException("Дружить с самим собой грустно и пошло");
        }

        user1.addFriend(user2Id);
        user2.addFriend(user1Id);
    }

    public void removeFromFriends(Long user1Id, Long user2Id) {
        userStorage.getEntityById(user1Id).removeFriend(user2Id);
        userStorage.getEntityById(user2Id).removeFriend(user1Id);
    }

    public List<User> getAllFriends(Long userId) {
        List<User> list = userStorage.getEntitiesBySet(userStorage.getEntityById(userId).getFriends());
        list.sort(Comparator.comparing(User::getId));
        return list;
    }

    public List<User> getMutualFriends(Long user1Id, Long user2Id) {
        final User user1 = userStorage.getEntityById(user1Id);
        if (user1 == null) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", user1Id));
        }

        final User user2 = userStorage.getEntityById(user2Id);
        if (user2 == null) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", user2Id));
        }

        final Set<Long> user1FriendsIds = user1.getFriends();
        final Set<Long> user2FriendsIds = user2.getFriends();

        Set<Long> mutualFriendsIds = user1FriendsIds
                .stream()
                .filter(user2FriendsIds::contains)
                .collect(Collectors.toSet());

        return userStorage.getEntitiesBySet(mutualFriendsIds);
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

    public User create(User user) {
        if (userStorage.getEntityById(user.getId()) != null) {
            throw new UserAlreadyExistsException(String.format("Пользователь с id = %s уже существует", user.getId()));
        }

        validation(user);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Пользователь без имени, имя = логин");
        }

        return userStorage.addEntity(user);
    }

    public User update(User user) {
        if (userStorage.getEntityById(user.getId()) == null) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", user.getId()));
        }

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