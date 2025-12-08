package com.ssafy.BlueStrongMountain.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class User {

    private final Long id;
    private final String email;
    private final String password;
    private final String username;
    private final String baekjoonHandle;
    private final UserStatus status;
    private final UserRole role;
    private final String refreshToken;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static User createNew(String email, String hashedPw, String username, String baekjoon){
        LocalDateTime now = LocalDateTime.now();
        return new User(
                null,
                email,
                hashedPw,
                username,
                baekjoon,
                UserStatus.ACTIVE,
                UserRole.USER,
                null,
                now,
                now
        );
    }
    public User withId(Long id){
        return new User(id, email, password, username, baekjoonHandle,
                status, role, refreshToken, createdAt, updatedAt);
    }
    public User withUsername(String newUsername) {
        LocalDateTime now = LocalDateTime.now();
        return new User(id, email, password, newUsername, baekjoonHandle, status, role, refreshToken, createdAt, now);
    }

    public User withPassword(String newHashedPw) {
        LocalDateTime now = LocalDateTime.now();
        return new User(id, email, newHashedPw, username, baekjoonHandle, status, role, refreshToken, createdAt, now);
    }

    public User withRefreshToken(String newToken) {
        LocalDateTime now = LocalDateTime.now();
        return new User(id, email, password, username, baekjoonHandle, status, role, newToken, createdAt, now);
    }

    public User inactive() {
        LocalDateTime now = LocalDateTime.now();
        return new User(id, email, password, username, baekjoonHandle, UserStatus.INACTIVE, role, null, createdAt, now);
    }




}
