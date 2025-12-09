package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.Board;
import com.ssafy.BlueStrongMountain.repository.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository{
    private final BoardMapper mapper;

    @Override
    public Board save(Board board) {
        if(board.getId() == null) {
            mapper.insert(board);
        }
        else{
            mapper.update(board);
        }
        return board;
    }

    @Override
    public Optional<Board> findById(Long boardId) {
        return Optional.ofNullable(mapper.findById(boardId));
    }

    @Override
    public List<Board> findByGroupId(Long groupId) {
        return mapper.findByGroupId(groupId);
    }

    @Override
    public void delete(Long boardId) {
        mapper.delete(boardId);
    }
}
