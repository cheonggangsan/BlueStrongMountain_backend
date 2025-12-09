package com.ssafy.BlueStrongMountain.repository.mapper;

import com.ssafy.BlueStrongMountain.domain.UserSolution;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserSolutionMapper {

    void save(UserSolution userSolution);

    List<UserSolution> findByUserId(Long userId);

    List<UserSolution> findByProblemId(Long problemId);

    Optional<UserSolution> findByUserAndProblem(Long userId, Long problemId);
}
