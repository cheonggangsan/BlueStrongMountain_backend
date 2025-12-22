package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.Problem;
import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.dto.ProblemFilterCondition;

import java.util.List;

public interface ProblemDBFilterService {
    List<ProblemDto> applyFilterFindByIds(List<Long>problemIds);
    List<ProblemDto> applyFilter(
            Long groupId,
            boolean unsolved,
            Integer option,
            ProblemFilterCondition problemFilterCondition
    );
}
