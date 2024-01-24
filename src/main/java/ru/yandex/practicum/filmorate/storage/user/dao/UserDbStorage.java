package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utility.UserMap;

import java.util.List;
import java.util.Objects;

@Component
@Primary
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert =
                new SimpleJdbcInsert(Objects.requireNonNull(jdbcTemplate.getDataSource()))
                        .withTableName("users").usingGeneratedKeyColumns("id");

        Long id = simpleJdbcInsert.executeAndReturnKey(user.toMapForDB()).longValue();
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        int result = jdbcTemplate.update("UPDATE users SET login = ?, name = ?," +
                        "email = ?, birthday = ? WHERE id = ?",
                user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        if (result == 0) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", user.getId()));
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE id = ?", UserMap::mapToUser, id);
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM USERS ORDER BY id", UserMap::mapToUser);
    }

    @Override
    public void deleteUserById(Long id) {
    }
}