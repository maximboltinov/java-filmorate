package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Builder
public class User implements Entity {
    private int id;
    @Email
    @NonNull
    private String email;
    @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;
}