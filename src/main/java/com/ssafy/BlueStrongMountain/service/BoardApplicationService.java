package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.BoardCreateRequest;
import com.ssafy.BlueStrongMountain.dto.BoardProgressResponse;
import com.ssafy.BlueStrongMountain.dto.BoardUpdateRequest;

public interface BoardApplicationService {

    /* ===============================
     * Board 생성
     * =============================== */
    Long createBoard(
            Long requesterId,
            Long groupId,
            BoardCreateRequest request
    );

    /* ===============================
     * Board 조회
     * =============================== */
    BoardProgressResponse getBoardProgress(
            Long requesterId,
            Long groupId,
            Long boardId
    );

    /* ===============================
     * Board 수정
     * =============================== */
    void updateBoard(
            Long requesterId,
            Long groupId,
            Long boardId,
            BoardUpdateRequest request
    );

    /* ===============================
     * Board 삭제
     * =============================== */
    void deleteBoard(
            Long requesterId,
            Long groupId,
            Long boardId
    );

    /* ===============================
     * Board 진행 동기화
     * =============================== */
    void syncBoardUserProgress(
            Long requesterId,
            Long groupId,
            Long boardId
    );
}
