package com.ssafy.BlueStrongMountain.repository.mapper;

import com.ssafy.BlueStrongMountain.domain.GroupRole;
import com.ssafy.BlueStrongMountain.domain.UserGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserGroupMapper {

    int insert(UserGroup userGroup);

    Optional<UserGroup> findByUserIdAndGroupId(
            @Param("userId") Long userId,
            @Param("groupId") Long groupId
    );

    List<UserGroup> findByUserId(@Param("userId") Long userId);

    List<UserGroup> findByGroupId(@Param("groupId") Long groupId);

    List<UserGroup> findByGroupIdAndRole(
            @Param("groupId") Long groupId,
            @Param("role") GroupRole role
    );

    int updateRole(
            @Param("userId") Long userId,
            @Param("groupId") Long groupId,
            @Param("role") GroupRole role
    );

    int deleteByUserIdAndGroupId(
            @Param("userId") Long userId,
            @Param("groupId") Long groupId
    );

    int deleteByGroupIdAndUserIds(
            @Param("groupId") Long groupId,
            @Param("userIds") List<Long> userIds
    );

    int countByGroupIdAndRole(
            @Param("groupId") Long groupId,
            @Param("role") GroupRole role
    );
}
