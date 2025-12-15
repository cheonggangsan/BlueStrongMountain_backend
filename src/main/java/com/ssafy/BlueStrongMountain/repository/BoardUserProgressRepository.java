package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.BoardUserProgress;
import com.ssafy.BlueStrongMountain.domain.BoardUserStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BoardUserProgressRepository {

    /* ===============================
     * 단건 조회 / 검증
     * =============================== */

    Optional<BoardUserProgress> find(
            Long boardId,
            Long userId,
            Long problemId
    );

    /* ===============================
     * INSERT
     * =============================== */

    void save(BoardUserProgress progress);

    void saveAll(List<BoardUserProgress> progresses);

    /* ===============================
     * UPDATE
     * =============================== */

    void updateStatus(
            Long boardId,
            Long userId,
            Long problemId,
            BoardUserStatus status,
            LocalDateTime solvedAt
    );

    /* ===============================
     * board + user → problem
     * =============================== */

    List<Long> findProblemIdsByBoardAndUser(
            Long boardId,
            Long userId
    );

    List<Long> findProblemIdsByBoardAndUserAndStatus(
            Long boardId,
            Long userId,
            BoardUserStatus status
    );

    /* ===============================
     * board + problem → user
     * =============================== */

    List<Long> findUserIdsByBoardAndProblem(
            Long boardId,
            Long problemId
    );

    List<Long> findUserIdsByBoardAndProblemAndStatus(
            Long boardId,
            Long problemId,
            BoardUserStatus status
    );

    /* ===============================
     * board → user / problem
     * =============================== */

    List<Long> findUserIdsByBoard(Long boardId);

    List<Long> findUserIdsByBoardAndStatus(
            Long boardId,
            BoardUserStatus status
    );

    List<Long> findProblemIdsByBoard(Long boardId);

    /* ===============================
     * row 단위 조회
     * =============================== */

    List<BoardUserProgress> findAllByBoardAndUser(
            Long boardId,
            Long userId
    );

    List<BoardUserProgress> findAllByBoard(Long boardId);

    /* ===============================
     * DELETE
     * =============================== */

    void deleteByBoard(Long boardId);

    void deleteByBoardAndUser(Long boardId, Long userId);

    void deleteByBoardAndUserAndProblem(
            Long boardId,
            Long userId,
            Long problemId
    );
}
