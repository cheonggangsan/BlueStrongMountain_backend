package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.ProblemDto;
import com.ssafy.BlueStrongMountain.dto.ProblemFilterRequest;
import java.util.List;

public interface ProblemFilterService {
    List<ProblemDto> applyFilters(List<ProblemDto> base, ProblemFilterRequest request);
}
