package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.BoardProblem;
import com.ssafy.BlueStrongMountain.repository.mapper.BoardProblemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardProblemRepositoryImpl implements BoardProblemRepository{

    private final BoardProblemMapper mapper;

    @Override
    public void deleteByBoardId(Long boardId) {
        mapper.deleteByBoardId(boardId);
    }

    @Override
    public void saveAll(List<BoardProblem> problems) {
        mapper.insertAll(problems);
    }

    @Override
    public List<BoardProblem> findByBoardId(Long boardId) {
        return mapper.findByBoardId(boardId);
    }
}
