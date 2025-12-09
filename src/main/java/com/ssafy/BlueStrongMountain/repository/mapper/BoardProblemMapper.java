package com.ssafy.BlueStrongMountain.repository.mapper;

import com.ssafy.BlueStrongMountain.domain.BoardProblem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardProblemMapper {

    void insert(BoardProblem boardProblem);

    void insertAll(@Param("list") List<BoardProblem> list);

    List<BoardProblem> findByBoardId(@Param("boardId") Long boardId);

    void deleteByBoardId(@Param("boardId") Long boardId);
}
