package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.dto.GroupUserDto;

import java.util.List;

public interface GroupMemberService {

    List<GroupUserDto> getAllUsers(Long requesterId, Long groupId);
    void addManagers(Long requesterId, Long groupId, List<Long> userIds);
    void removeManagers(Long requesterId, Long groupId, List<Long> managerIds);
    void addMembers(Long requesterId, Long groupId, List<Long> userIds);
    void removeMembers(Long requesterId, Long groupId, List<Long> userIds);
    void leaveGroup(Long requesterId, Long groupId);
}
