package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.UserSolution;
import com.ssafy.BlueStrongMountain.repository.mapper.UserSolutionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserSolutionRepositoryImpl implements UserSolutionRepository {

    private final UserSolutionMapper mapper;

    @Override
    public UserSolution save(UserSolution userSolution) {
        mapper.save(userSolution);
        return userSolution;
    }

    @Override
    public List<UserSolution> findByUserId(Long userId) {
        return mapper.findByUserId(userId);
    }

    @Override
    public List<UserSolution> findByProblemId(Long problemId) {
        return mapper.findByProblemId(problemId);
    }

    @Override
    public Optional<UserSolution> findByUserAndProblem(Long userId, Long problemId) {
        return mapper.findByUserAndProblem(userId, problemId);
    }
}
