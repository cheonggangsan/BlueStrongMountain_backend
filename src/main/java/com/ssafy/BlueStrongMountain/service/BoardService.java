package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.*;

import java.util.List;

public interface BoardService {
    Long createBoard(Long requesterId, Long groupId, BoardCreateRequest request);
    List<BoardResponse> getBoards(Long groupId, BoardSearchCondition condition);
    BoardDetailResponse getBoard(Long groupId, Long boardId);
    void updateBoard(Long requesterId, Long groupId, Long boardId, BoardUpdateRequest request);
    void deleteBoard(Long requesterId, Long groupId, Long boardId);
}
