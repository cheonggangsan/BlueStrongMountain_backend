package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.Group;
import java.util.List;
import java.util.Optional;

public interface GroupRepository {
    List<Long> findUserIdsByGroupId(Long groupId);
    List<Long> findGroupSolvedProblems(Long groupId);
    List<Long> findUsersSolvedProblems(Long groupId);

    Group save(Group group);
    Optional<Group> findById(Long groupId);
    List<Group> findByIds(List<Long> groupIds);
    List<Group> findByTitleLike(String titleKeyword);
    void deleteById(Long groupId);
}
