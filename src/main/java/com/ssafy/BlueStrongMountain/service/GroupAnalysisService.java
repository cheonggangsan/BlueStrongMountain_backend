package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;

import java.util.List;
import java.util.Map;

public interface GroupAnalysisService {


    Map<String, Long> countSolvedProblemTagsByGroupId(Long groupId);
    List<ProblemDto> fetchProblemsByGroupId(Long groupId);
}
