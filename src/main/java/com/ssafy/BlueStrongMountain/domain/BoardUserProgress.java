package com.ssafy.BlueStrongMountain.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardUserProgress {

    public enum Status {
        PENDING, SOLVED
    }

    private Long id;
    private Long boardId;
    private Long userId;
    private Long problemId;
    private Status status;
    private LocalDateTime solvedAt;

    public BoardUserProgress(Long boardId, Long userId, Long problemId) {
        this.boardId = boardId;
        this.userId = userId;
        this.problemId = problemId;
        this.status = Status.PENDING;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void markSolved() {
        this.status = Status.SOLVED;
        this.solvedAt = LocalDateTime.now();
    }
}
