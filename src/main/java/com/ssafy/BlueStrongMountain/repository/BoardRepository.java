package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);
    Optional<Board> findById(Long boardId);
    List<Board> findByGroupId(Long groupId);
    void delete(Long boardId);
}
