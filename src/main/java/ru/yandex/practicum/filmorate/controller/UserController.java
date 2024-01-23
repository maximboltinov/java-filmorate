package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Пришел запрос POST /users");
        final User userResponse = userService.create(user);
        log.info("Отправлен ответ POST /users {}", userResponse);
        return userResponse;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("Пришел запрос GET /users/{}", id);
        final User user = userService.getUserById(id);
        log.info("Отправлен ответ GET /users/{} {}", id, user);
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Пришел запрос GET /users");
        final List<User> usersList = userService.getAll();
        log.info("Отправлен ответ GET /users {}", usersList);
        return usersList;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Пришел запрос PUT /users");
        final User userResponse = userService.update(user);
        log.info("Отправлен ответ PUT /users {}", userResponse);
        return userResponse;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Пришел запрос GET /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
        log.info("Отправлен ответ GET /users/{}/friends/{}", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable Long id) {
        log.info("Пришел запрос GET /users/{}/friends", id);
        List<User> userList = userService.getAllFriends(id);
        log.info("Отправлен ответ GET /users/{}/friends {}", id, userList);
        return userList;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Пришел запрос DELETE /users/{}/friends/{}", id, friendId);
        userService.removeFromFriends(id, friendId);
        log.info("Отправлен ответ DELETE /users/{}/friends/{}", id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Пришел запрос GET /users/{}/friends/common/{}", id, otherId);
        List<User> userList = userService.getMutualFriends(id, otherId);
        log.info("Отправлен ответ GET /users/{}/friends/common/{} {}", id, otherId, userList);
        return userList;
    }
}