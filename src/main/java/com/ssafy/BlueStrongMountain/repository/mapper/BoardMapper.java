package com.ssafy.BlueStrongMountain.repository.mapper;

import com.ssafy.BlueStrongMountain.domain.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {

    void insert(Board board);

    void update(Board board);

    Board findById(@Param("id") Long id);

    List<Board> findByGroupId(@Param("groupId") Long groupId);

    void delete(@Param("id") Long id);
}
