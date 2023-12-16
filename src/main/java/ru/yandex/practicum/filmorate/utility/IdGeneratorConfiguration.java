package ru.yandex.practicum.filmorate.utility;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfiguration {

    @Bean("idGeneratorForUsers")
    public IdGenerator gen1() {
        return new IdGenerator();
    }

    @Bean("idGeneratorForFilms")
    public IdGenerator gen2() {
        return new IdGenerator();
    }
}