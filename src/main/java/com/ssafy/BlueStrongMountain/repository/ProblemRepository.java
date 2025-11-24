package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import java.util.List;
import java.util.Set;

public interface ProblemRepository {
    List<ProblemDto> findAll();

    List<ProblemDto> findByIdList(List<Long> ids);

    Set<Long> findSolvedProblemsByGroupUsers(Long groupId);

    List<Long> findReviewProblemIdsByGroup(Long groupId);
}
