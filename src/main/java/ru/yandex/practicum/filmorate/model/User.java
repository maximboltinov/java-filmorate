package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class User {
    private Long id;
    @Email
    @NonNull
    private String email;
    @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;

    public Map<String, Object> toMapForDB() {
        return Map.of("login", login, "name", name, "email", email,
                "birthday", java.sql.Date.valueOf(birthday));
    }
}