package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.dto.ProblemFilterCondition;

import java.util.List;
import java.util.Set;

public interface ProblemRepository {
    List<ProblemDto> findAll();

    List<ProblemDto> findByIdList(List<Long> ids);

    List<ProblemDto> findByCondition(
            Long groupId,
            boolean unsolved,
            ProblemFilterCondition cond
    );
}
