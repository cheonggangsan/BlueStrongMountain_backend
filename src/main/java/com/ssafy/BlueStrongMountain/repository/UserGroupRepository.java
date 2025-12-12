package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.GroupRole;
import com.ssafy.BlueStrongMountain.domain.UserGroup;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository {
    //UserGroup save(UserGroup userGroup);
    void insert(UserGroup userGroup);
    void updateRole(UserGroup userGroup);
    List<UserGroup> findByUserId(Long userId);
    List<UserGroup> findByGroupId(Long groupId);
    Optional<UserGroup> findByUserIdAndGroupId(Long userId, Long groupId);
    void deleteByUserIdAndGroupId(Long userId, Long groupId);
    void deleteByGroupIdAndUserIdIn(Long groupId, List<Long> userIds);
    int countByGroupId(Long groupId);
    List<UserGroup> findByGroupIdAndRole(Long groupId, GroupRole role);
}
