package ru.yandex.practicum.filmorate.storage.friends.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.UserMap;

import java.util.List;

@Component
@AllArgsConstructor
public class FriendsDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public void addFriend(Long userId, Long friendId) {
        jdbcTemplate.update("INSERT INTO USER_FRIEND (USER_ID, FRIEND_ID) VALUES (?, ?)", userId, friendId);
    }

    public List<User> getAllFriends(Long userId) {
        return jdbcTemplate.query("SELECT * FROM USERS " +
                        "WHERE ID IN (SELECT FRIEND_ID FROM USER_FRIEND WHERE USER_ID = ?) " +
                        "ORDER BY ID",
                UserMap::mapToUser, userId);
    }

    public List<User> getMutualFriends(Long user1Id, Long user2Id) {
        return jdbcTemplate.query("SELECT * FROM USERS WHERE ID IN " +
                        "(SELECT FRIEND_ID FROM USER_FRIEND WHERE USER_ID = ? AND FRIEND_ID IN " +
                        "(SELECT FRIEND_ID FROM user_friend WHERE USER_ID = ?))",
                UserMap::mapToUser, user1Id, user2Id);
    }

    public void removeFromFriends(Long user1Id, Long user2Id) {
        jdbcTemplate.update("DELETE FROM USER_FRIEND WHERE USER_ID = ? AND FRIEND_ID = ?",
                user1Id, user2Id);
    }
}