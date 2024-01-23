package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@Primary
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addEntity(User user) {
        SimpleJdbcInsert simpleJdbcInsert =
                new SimpleJdbcInsert(Objects.requireNonNull(jdbcTemplate.getDataSource()))
                        .withTableName("users").usingGeneratedKeyColumns("id");

        Long id = simpleJdbcInsert.executeAndReturnKey(user.toMapForDB()).longValue();
        user.setId(id);
        return user;
    }

    @Override
    public User updateEntity(User user) {
        int result = jdbcTemplate.update("UPDATE users SET login = ?, name = ?," +
                        "email = ?, birthday = ? WHERE id = ?",
                user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        if (result == 0) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", user.getId()));
        }
        return user;
    }

    @Override
    public User getEntityById(Long id) {
        User user;

        try {
            user = jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE id = ?", this::mapToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id = %s", id));
        }

        return user;
    }

    @Override
    public List<User> getAllEntities() {
        return jdbcTemplate.query("SELECT * FROM USERS ORDER BY id", this::mapToUser);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        jdbcTemplate.update("INSERT INTO USER_FRIEND (USER_ID, FRIEND_ID) VALUES (?, ?)", userId, friendId);
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        return jdbcTemplate.query("SELECT * FROM USERS " +
                        "WHERE ID IN (SELECT FRIEND_ID FROM USER_FRIEND WHERE USER_ID = ?) " +
                        "ORDER BY ID",
                this::mapToUser, userId);
    }

    @Override
    public List<User> getMutualFriends(Long user1Id, Long user2Id) {
        return jdbcTemplate.query("SELECT * FROM USERS WHERE ID IN " +
                        "(SELECT FRIEND_ID FROM USER_FRIEND WHERE USER_ID = ? AND FRIEND_ID IN " +
                        "(SELECT FRIEND_ID FROM user_friend WHERE USER_ID = ?))",
                this::mapToUser, user1Id, user2Id);
    }

    @Override
    public void removeFromFriends(Long user1Id, Long user2Id) {
        jdbcTemplate.update("DELETE FROM USER_FRIEND WHERE USER_ID = ? AND FRIEND_ID = ?",
                user1Id, user2Id);
    }

    @Override
    public List<User> getEntitiesBySet(Set<Long> usersIds) {
        return null;
    }

    @Override
    public void deleteEntityById(Long id) {

    }

    private User mapToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate()).build();
    }
}