package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    protected final Map<Long, User> storage = new HashMap<>();

    @Override
    public User addEntity(User user) {
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
    public boolean isEntityRegistered(Long id) {
        return storage.containsKey(id);
    }

    @Override
    public List<User> getAllEntities() {
        return new ArrayList<>(storage.values());
    }
}