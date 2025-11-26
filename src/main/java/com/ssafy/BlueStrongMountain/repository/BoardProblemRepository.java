package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.BoardProblem;

import java.util.List;

public interface BoardProblemRepository {
    void deleteByBoardId(Long boardId);
    void saveAll(List<BoardProblem> problems);
    List<BoardProblem> findByBoardId(Long boardId);
}
