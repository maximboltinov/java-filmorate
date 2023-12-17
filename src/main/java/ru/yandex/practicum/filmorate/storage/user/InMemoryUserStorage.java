package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Long counter = 1L;
    protected final Map<Long, User> storage = new HashMap<>();

    @Override
    public User addEntity(User user) {
        user.setId(generateId());
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateEntity(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User getEntityById(Long id) {
        return storage.get(id);
    }

    @Override
    public void deleteEntityById(Long id) {
        storage.remove(id);
    }

    @Override
    public List<User> getEntitiesBySet(Set<Long> usersIds) {
        return new ArrayList<>(storage.entrySet()
                .stream()
                .filter(entry -> usersIds.contains(entry.getKey()))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue))
                .values()
        );
    }

    @Override
    public List<User> getAllEntities() {
        List<User> list = new ArrayList<>(storage.values());
        list.sort(Comparator.comparing(User::getId));
        return list;
    }

    private Long generateId() {
        return counter++;
    }
}