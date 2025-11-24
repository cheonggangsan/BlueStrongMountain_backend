package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.dto.GroupProblemDto;
import com.ssafy.BlueStrongMountain.dto.UserSolvedDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MockGroupRepository implements GroupRepository{
    private static final List<GroupProblemDto> MOCK_GROUP_SOLVED_PROBLEMS = List.of(
            new GroupProblemDto(1L, 1003L, 1),
            new GroupProblemDto(1L, 1005L, 2),
            new GroupProblemDto(1L, 1009L, 3)
    );
    private static final List<UserSolvedDto> MOCK_EACH_USERS_SOLVED_PROBLEMS = List.of(
            new UserSolvedDto(1L, 1003L, 1L),
            new UserSolvedDto(2L, 1003L, 1L),
            new UserSolvedDto(1L, 1005L, 1L),
            new UserSolvedDto(1L, 1409L, 1L),
            new UserSolvedDto(2L, 2004L, 1L)
    );

    @Override
    public List<Long> findUserIdsByGroupId(Long groupId) {
        return List.of(1L, 2L);
    }

    @Override
    public List<Long> findGroupSolvedProblems(Long groupId) {
        return MOCK_GROUP_SOLVED_PROBLEMS.stream()
                .map(GroupProblemDto::getProblemId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> findUsersSolvedProblems(Long groupId) {
        return MOCK_EACH_USERS_SOLVED_PROBLEMS.stream()
                .map(UserSolvedDto::getProblemId)
                .collect(Collectors.toList());
    }
}
