package com.ssafy.BlueStrongMountain.domain;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class UserSolution {

    private final Long id;
    private final Long userId;
    private final Long problemId;
    private final LocalDateTime solvedAt;
    private final LocalDateTime createdAt;

    private UserSolution(Long id, Long userId, Long problemId,
                         LocalDateTime solvedAt, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.problemId = problemId;
        this.solvedAt = solvedAt;
        this.createdAt = createdAt;
    }
    public static UserSolution create(Long userId, Long problemId, LocalDateTime now) {
        return new UserSolution(
                null,
                userId,
                problemId,
                now,
                now
        );
    }
}
