package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int idCounter = 1;

    @GetMapping
    public List<User> getAll() {
        log.info("Пришел запрос GET /users");
        final List<User> usersList = new ArrayList<>(users.values());
        log.info("Отправлен ответ GET /users {}", usersList);
        return usersList;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Пришел запрос POST /users");

        if (users.containsKey(user.getId())) {
            ValidationException e = new ValidationException("Такой пользователь уже существует");
            log.debug("Запрос завершен ошибкой: " + e.getMessage());
            throw e;
        }

        try {
            userValidation(user);
        } catch (ValidationException e) {
            log.info("Не прошел валидацию с ошибкой: " + e.getMessage());
            throw e;
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Пользователь без имени, имя = логин");
        }

        final int id = idCounter++;
        user.setId(id);
        users.put(id, user);
        log.info("Отправлен ответ POST /users {}", users.get(id));
        return users.get(id);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Пришел запрос PUT /users");

        final int id = user.getId();
        if (!users.containsKey(id)) {
            ValidationException e = new ValidationException("Пользователь не найден");
            log.debug("Запрос завершен ошибкой: " + e.getMessage());
            throw e;
        }

        try {
            userValidation(user);
        } catch (ValidationException e) {
            log.info("Не прошел валидацию с ошибкой: " + e.getMessage());
            throw e;
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Пользователь без имени, имя = логин");
        }

        users.put(id, user);
        log.info("Отправлен ответ PUT /users {}", users.get(id));
        return users.get(id);
    }

    private void userValidation(User user) {
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