package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.UserSolution;

import java.util.List;
import java.util.Optional;

public interface UserSolutionService {

    UserSolution save(UserSolution userSolution);

    List<UserSolution> findByUserId(Long userId);

    List<UserSolution> findByProblemId(Long problemId);

    Optional<UserSolution> findByUserAndProblem(Long userId, Long problemId);

    // 불필요 중복 방지용 (선택)
    boolean hasSolved(Long userId, Long problemId);

    // problemId만 추출하는 편의 메서드 (자주 쓰임)
    List<Long> findSolvedProblemIds(Long userId);
}
