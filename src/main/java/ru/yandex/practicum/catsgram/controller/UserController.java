package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (users
                .values()
                .stream()
                .anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))
        ) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            throw new ConditionsNotMetException("Пользователь не найден");
        }
        String newEmail = newUser.getEmail();
        if (newEmail != null) {
            if (users
                    .values()
                    .stream()
                    .anyMatch(user1 -> user1.getEmail().equals(newEmail))
            ) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
        }
        if (newEmail != null) {
            oldUser.setEmail(newEmail);
        }
        String newUsername = newUser.getUsername();
        if (newUsername != null) {
            oldUser.setUsername(newUsername);
        }
        String newPassword = newUser.getPassword();
        if (newPassword != null) {
            oldUser.setPassword(newPassword);
        }
        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
