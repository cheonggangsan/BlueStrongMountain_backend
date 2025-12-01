package com.ssafy.BlueStrongMountain.service;

import java.util.List;

public interface GroupMemberService {
    void addManagers(Long requesterId, Long groupId, List<Long> userIds);
    void removeManagers(Long requesterId, Long groupId, List<Long> managerIds);
    void addMembers(Long requesterId, Long groupId, List<Long> userIds);
    void removeMembers(Long requesterId, Long groupId, List<Long> userIds);
    void leaveGroup(Long requesterId, Long groupId);
}
