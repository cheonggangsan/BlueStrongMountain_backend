package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.GroupRole;
import com.ssafy.BlueStrongMountain.domain.UserGroup;
import com.ssafy.BlueStrongMountain.repository.mapper.UserGroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserGroupRepositoryImpl implements UserGroupRepository {

    private final UserGroupMapper mapper;

    @Override
    public UserGroup save(UserGroup userGroup) {
        mapper.insert(userGroup);
        return userGroup;
    }

    @Override
    public List<UserGroup> findByUserId(Long userId) {
        return mapper.findByUserId(userId);
    }

    @Override
    public List<UserGroup> findByGroupId(Long groupId) {
        return mapper.findByGroupId(groupId);
    }

    @Override
    public Optional<UserGroup> findByUserIdAndGroupId(Long userId, Long groupId) {
        return mapper.findByUserIdAndGroupId(userId, groupId);
    }

    @Override
    public void deleteByUserIdAndGroupId(Long userId, Long groupId) {
        mapper.deleteByUserIdAndGroupId(userId, groupId);
    }

    @Override
    public void deleteByGroupIdAndUserIdIn(Long groupId, List<Long> userIds) {
        mapper.deleteByGroupIdAndUserIds(groupId, userIds);
    }

    @Override
    public int countByGroupId(Long groupId) {
        return mapper.countByGroupIdAndRole(groupId, GroupRole.MEMBER);
    }

    @Override
    public List<UserGroup> findByGroupIdAndRole(Long groupId, GroupRole role) {
        return mapper.findByGroupIdAndRole(groupId, role);
    }
}

