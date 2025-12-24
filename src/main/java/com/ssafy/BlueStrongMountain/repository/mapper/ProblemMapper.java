package com.ssafy.BlueStrongMountain.repository.mapper;

import com.ssafy.BlueStrongMountain.domain.Problem;
import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface ProblemMapper {
    List<ProblemDto> findAll();
    List<ProblemDto> findByIdList(@Param("ids") List<Long> ids);
    List<ProblemDto> findProblems(
            @Param("difficultyFrom") Integer difficultyFrom,
            @Param("difficultyTo") Integer difficultyTo,
            @Param("tags") List<String> tags,
            @Param("minSolvers") Integer minSolvers
    );

    List<ProblemDto> findUnsolvedProblemsByGroup(
            @Param("groupId") Long groupId,
            @Param("difficultyFrom") Integer difficultyFrom,
            @Param("difficultyTo") Integer difficultyTo,
            @Param("tags") List<String> tags,
            @Param("minSolvers") Integer minSolvers
    );


}
