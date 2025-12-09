package com.ssafy.BlueStrongMountain.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Problem {

    private final Long id;
    private final String title;
    private final Integer difficulty;
    private final Integer acceptedUserCount;
    private final LocalDateTime lastSyncedAt;

    public Problem(Long id,
                   String title,
                   Integer difficulty,
                   Integer acceptedUserCount,
                   LocalDateTime lastSyncedAt) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.acceptedUserCount = acceptedUserCount;
        this.lastSyncedAt = lastSyncedAt;
    }

    public Problem withTitle(String title) {
        return new Problem(this.id, title, this.difficulty, this.acceptedUserCount, this.lastSyncedAt);
    }

    public Problem withDifficulty(Integer difficulty) {
        return new Problem(this.id, this.title, difficulty, this.acceptedUserCount, this.lastSyncedAt);
    }

    public Problem withAcceptedUserCount(Integer acceptedUserCount) {
        return new Problem(this.id, this.title, this.difficulty, acceptedUserCount, this.lastSyncedAt);
    }

    public Problem withLastSyncedAt(LocalDateTime lastSyncedAt) {
        return new Problem(this.id, this.title, this.difficulty, this.acceptedUserCount, lastSyncedAt);
    }
}
