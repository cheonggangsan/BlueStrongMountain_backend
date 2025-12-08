package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

//@Repository
public class InMemoryUserRepository implements UserRepository{

    private final AtomicLong seq = new AtomicLong(0);
    private final Map<Long, User> store = new HashMap<>();
    private final Map<String, Long> emailIndex = new HashMap<>();
    private final Map<String, Long> usernameIndex = new HashMap<>();

    @Override
    public User save(User user) {
        Long id = user.getId();
        if(id == null) id = seq.incrementAndGet();

        User savedUser = user.withId(id);
        store.put(id, savedUser);

        emailIndex.put(savedUser.getEmail(), id);
        usernameIndex.put(savedUser.getUsername(), id);

        return savedUser;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Long id = emailIndex.get(email);
        if(id == null) return Optional.empty();
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Long id = usernameIndex.get(username);
        if (id == null) return Optional.empty();
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void delete(Long id) {
        User user = store.remove(id);
        if (user == null) return;
        emailIndex.remove(user.getEmail());
        usernameIndex.remove(user.getUsername());
    }
}
