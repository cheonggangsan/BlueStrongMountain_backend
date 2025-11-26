package com.ssafy.BlueStrongMountain.repository;

import java.util.List;

public interface GroupRepository {
    List<Long> findUserIdsByGroupId(Long groupId);
    List<Long> findGroupSolvedProblems(Long groupId);
    List<Long> findUsersSolvedProblems(Long groupId);
}
