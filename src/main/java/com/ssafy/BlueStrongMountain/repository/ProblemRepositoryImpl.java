package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.dto.ProblemFilterCondition;
import com.ssafy.BlueStrongMountain.repository.mapper.ProblemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProblemRepositoryImpl implements ProblemRepository {

    private final ProblemMapper problemMapper;

    @Override
    public List<ProblemDto> findAll() {
        return problemMapper.findAll();
    }

    @Override
    public List<ProblemDto> findByIdList(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return problemMapper.findByIdList(ids);
    }

    @Override
    public List<ProblemDto> findByCondition(
            Long groupId,
            boolean unsolved,
            ProblemFilterCondition cond
    ) {
        if (unsolved) {
            return problemMapper.findUnsolvedProblemsByGroup(
                    groupId,
                    cond.getDifficultyFrom(),
                    cond.getDifficultyTo(),
                    cond.getTags(),
                    cond.getMinSolvers()
            );
        }
        return problemMapper.findProblems(
                cond.getDifficultyFrom(),
                cond.getDifficultyTo(),
                cond.getTags(),
                cond.getMinSolvers()
        );
    }
}
