package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NoUserException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.LocalDateAdapter;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private static int idCounter = 1;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe())
            .create();

    @GetMapping
    public String findAll() {
        return gson.toJson(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        String message = userValidation(user);
        if (!message.isEmpty()) {
            log.debug("Создание: не прошел валидацию: " + message);
            throw new ValidationException(message);
        }
        if (users.containsKey(user.getId())) {
            log.debug("Создание: повтор пользователя");
            throw new UserAlreadyExistException("Такой пользователь уже существует");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        final int id = idCounter++;
        user.setId(id);
        users.put(id, user);
        log.info("Создание: добавлен пользователь с id: " + id);
        return users.get(id);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        String message = userValidation(user);
        if (!message.isEmpty()) {
            log.debug("Обновление: не прошел валидацию: " + message);
            throw new ValidationException(message);
        }
        int id = user.getId();
        if (!users.containsKey(id)) {
            log.debug("Обновление: пользователь отсутствует");
            throw new NoUserException("такого пользователя не существует");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(id, user);
        log.info("Обновление: обновлен пользователь с id: " + id);
        return users.get(id);
    }

    private String userValidation(User user) {
        StringBuilder stringBuilder = new StringBuilder();
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            stringBuilder.append("некорректный email");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(" & ");
            }
            stringBuilder.append("некорректный логин");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(" & ");
            }
            stringBuilder.append("некорректная дата рождения");
        }
        return stringBuilder.toString();
    }
}