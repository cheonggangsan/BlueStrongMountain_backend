package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    List<User> searchByEmail(String keyword);
    List<User> searchByUsername(String keyword);
    void delete(Long id);
}
