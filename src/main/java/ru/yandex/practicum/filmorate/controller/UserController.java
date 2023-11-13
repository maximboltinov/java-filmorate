package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends Controller<User> {
    @Override
    @GetMapping
    public List<User> getAll() {
        log.info("Пришел запрос GET /users");
        final List<User> usersList = super.getAll();
        log.info("Отправлен ответ GET /users {}", usersList);
        return usersList;
    }

    @Override
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Пришел запрос POST /users");

        if (storage.containsKey(user.getId())) {
            ValidationException e = new ValidationException("Такой пользователь уже существует");
            log.debug("Запрос завершен ошибкой: " + e.getMessage());
            throw e;
        }

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

        final User userResponse = super.create(user);
        log.info("Отправлен ответ POST /users {}", userResponse);
        return userResponse;
    }

    @Override
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Пришел запрос PUT /users");

        if (!storage.containsKey(user.getId())) {
            ValidationException e = new ValidationException("Пользователь не найден");
            log.debug("Запрос завершен ошибкой: " + e.getMessage());
            throw e;
        }

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

        final User userResponse = super.update(user);
        log.info("Отправлен ответ PUT /users {}", userResponse);
        return userResponse;
    }

    @Override
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