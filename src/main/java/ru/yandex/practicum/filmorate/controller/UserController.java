package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Пришел запрос GET /users");
        final List<User> usersList = userService.getAll();
        log.info("Отправлен ответ GET /users {}", usersList);
        return usersList;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Пришел запрос POST /users");

        try {
            validation(user);
        } catch (ValidationException e) {
            log.info("Не прошел валидацию с ошибкой: " + e.getMessage());
            throw e;
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Пользователь без имени, имя = логин");
        }

        final User userResponse = userService.create(user);
        log.info("Отправлен ответ POST /users {}", userResponse);
        return userResponse;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Пришел запрос PUT /users");

        try {
            validation(user);
        } catch (ValidationException e) {
            log.info("Не прошел валидацию с ошибкой: " + e.getMessage());
            throw e;
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Пользователь без имени, имя = логин");
        }

        final User userResponse = userService.update(user);
        log.info("Отправлен ответ PUT /users {}", userResponse);
        return userResponse;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable Long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getMutualFriends(id, otherId);
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