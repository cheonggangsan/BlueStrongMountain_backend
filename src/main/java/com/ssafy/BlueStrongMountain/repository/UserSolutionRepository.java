package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.UserSolution;

import java.util.List;
import java.util.Optional;

public interface UserSolutionRepository {

    UserSolution save(UserSolution userSolution);

    List<UserSolution> findByUserId(Long userId);

    List<UserSolution> findByProblemId(Long problemId);

    Optional<UserSolution> findByUserAndProblem(Long userId, Long problemId);
}
