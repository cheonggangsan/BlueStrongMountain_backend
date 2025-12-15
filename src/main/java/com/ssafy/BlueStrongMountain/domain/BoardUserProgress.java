package com.ssafy.BlueStrongMountain.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardUserProgress {


    private Long id;
    private Long boardId;
    private Long userId;
    private Long problemId;
    private BoardUserStatus status;
    private LocalDateTime solvedAt;

    public BoardUserProgress(Long boardId, Long userId, Long problemId) {
        this.boardId = boardId;
        this.userId = userId;
        this.problemId = problemId;
        this.status = BoardUserStatus.PENDING;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void markSolved() {
        this.status = BoardUserStatus.SOLVED;
        this.solvedAt = LocalDateTime.now();
    }
}
