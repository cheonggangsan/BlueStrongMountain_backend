package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.BoardUserProgress;

import java.util.List;

public interface BoardUserProgressService {

    /* ===============================
     * 초기화
     * =============================== */

    void initializeBoardProgress(
            Long boardId,
            Long groupId,
            List<Long> problemIds
    );

    /* ===============================
     * 문제 풀이 처리
     * =============================== */

    void markSolved(
            Long boardId,
            Long userId,
            Long problemId
    );

    /* ===============================
     * 조회
     * =============================== */

    List<Long> getSolvedProblemIds(
            Long boardId,
            Long userId
    );

    List<Long> getPendingProblemIds(
            Long boardId,
            Long userId
    );

    List<BoardUserProgress> getProgressByBoardAndUser(
            Long boardId,
            Long userId
    );

    List<BoardUserProgress> getProgressByBoard(Long boardId);


    /* ===============================
     * 삭제
     * =============================== */
    void deleteByBoard(Long boardId);

    /* ===============================
     * 동기화 (보드 수정 등)
     * =============================== */

    void updateBoardProgress(
            Long boardId,
            Long groupId,
            List<Long> addedProblemIds,
            List<Long> removedProblemIds
    );
}
