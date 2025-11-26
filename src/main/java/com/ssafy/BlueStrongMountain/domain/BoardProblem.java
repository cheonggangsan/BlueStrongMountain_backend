package com.ssafy.BlueStrongMountain.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardProblem {

    private final Long id;
    private final Long boardId;
    private final Long problemId;
    private final Integer orderIndex;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private BoardProblem(Long id, Long boardId, Long problemId, Integer orderIndex,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.boardId = boardId;
        this.problemId = problemId;
        this.orderIndex = orderIndex;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BoardProblem create(Long boardId, Long problemId, Integer orderIndex) {
        return new BoardProblem(
                null,
                boardId,
                problemId,
                orderIndex,
                LocalDateTime.now(),
                null
        );
    }

    public BoardProblem assignId(Long id) {
        return new BoardProblem(
                id,
                this.boardId,
                this.problemId,
                this.orderIndex,
                this.createdAt,
                this.updatedAt
        );
    }

}

