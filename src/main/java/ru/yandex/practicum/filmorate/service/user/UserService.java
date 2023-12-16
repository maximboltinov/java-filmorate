package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utility.IdGenerator;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    private final IdGenerator idGenerator;

    @Autowired
    public UserService(UserStorage userStorage,
                       @Qualifier("idGeneratorForUsers") IdGenerator idGenerator) {
        this.userStorage = userStorage;
        this.idGenerator = idGenerator;
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
        return userStorage.getEntitiesBySet(userStorage.getEntityById(userId).getFriends());
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
        if (userStorage.isEntityRegistered(user.getId())) {
            throw new UserAlreadyExistsException(String.format("Пользователь с id = %s уже существует", user.getId()));
        }

        Long id = idGenerator.generateId();
        log.info(String.format("создан пользователь с id %s", id));
        user.setId(id);
        return userStorage.addEntity(user);
    }

    public User update(User user) {
        if (!userStorage.isEntityRegistered(user.getId())) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", user.getId()));
        }
        return userStorage.addEntity(user);
    }

    public boolean isUserRegistered(Long id) {
        return userStorage.isEntityRegistered(id);
    }
}