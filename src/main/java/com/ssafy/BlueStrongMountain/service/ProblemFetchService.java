package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import java.util.List;

public interface ProblemFetchService {
    List<ProblemDto> fetchBaseProblems(Long groupId, Boolean unsolved);

    List<ProblemDto> fetchReviewProblems(Long groupId);
}
