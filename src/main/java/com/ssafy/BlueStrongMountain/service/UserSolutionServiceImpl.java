package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.UserSolution;
import com.ssafy.BlueStrongMountain.repository.UserSolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSolutionServiceImpl implements UserSolutionService {

    private final UserSolutionRepository userSolutionRepository;

    @Override
    public UserSolution save(UserSolution userSolution) {
        return userSolutionRepository.save(userSolution);
    }

    @Override
    public List<UserSolution> findByUserId(Long userId) {
        return userSolutionRepository.findByUserId(userId);
    }

    @Override
    public List<UserSolution> findByProblemId(Long problemId) {
        return userSolutionRepository.findByProblemId(problemId);
    }

    @Override
    public Optional<UserSolution> findByUserAndProblem(Long userId, Long problemId) {
        return userSolutionRepository.findByUserAndProblem(userId, problemId);
    }

    @Override
    public boolean hasSolved(Long userId, Long problemId) {
        return userSolutionRepository.findByUserAndProblem(userId, problemId).isPresent();
    }

    @Override
    public List<Long> findSolvedProblemIds(Long userId) {
        List<UserSolution> solutions = userSolutionRepository.findByUserId(userId);

        List<Long> ids = new ArrayList<>();

        for (UserSolution s : solutions) {
            ids.add(s.getProblemId());
        }

        return ids;
    }
}

