package ru.yandex.practicum.filmorate.service.friends;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.dao.FriendsDbStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FriendsService {
    private final FriendsDbStorage friendsDbStorage;

    public void addFriend(Long userId, Long friendId) {
        try {
            friendsDbStorage.addFriend(userId, friendId);
        } catch (DataIntegrityViolationException e) {
            throw new UserNotFoundException("Не найден id");
        }
    }

    public void removeFromFriends(Long user1Id, Long user2Id) {
        friendsDbStorage.removeFromFriends(user1Id, user2Id);
    }

    public List<User> getAllFriends(Long userId) {
        return friendsDbStorage.getAllFriends(userId);
    }

    public List<User> getMutualFriends(Long user1Id, Long user2Id) {
        return friendsDbStorage.getMutualFriends(user1Id, user2Id);
    }
}