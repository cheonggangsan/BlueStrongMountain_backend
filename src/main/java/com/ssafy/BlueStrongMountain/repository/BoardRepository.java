package com.ssafy.BlueStrongMountain.repository;

import java.util.List;

public interface BoardRepository {
    List<Long> findBoardIdsByGroupId(Long groupId);
    List<Long> findProblemIdsByBoards(List<Long> boardIds);
}
