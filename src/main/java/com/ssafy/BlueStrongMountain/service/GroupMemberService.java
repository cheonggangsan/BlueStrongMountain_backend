package com.ssafy.BlueStrongMountain.service;

public interface GroupMemberService {
    void addManager(Long requesterId, Long groupId, Long userId);
    void removeManagers(Long requesterId, Long groupId, java.util.List<Long> managerIds);
    void addMember(Long requesterId, Long groupId, Long userId);
    void removeMember(Long requesterId, Long groupId, Long memberId);
    void leaveGroup(Long requesterId, Long groupId);
}
