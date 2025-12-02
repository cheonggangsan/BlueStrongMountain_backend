package com.ssafy.BlueStrongMountain.service.validator;

public interface GroupAuthorityService {
    void validateOwner(Long userId, Long groupId);
    void validateManager(Long userId, Long groupId);
    void validateGroupMember(Long userId, Long groupId);
    void validateNotOwner(Long userId, Long groupId);
    void validateUserInGroup(Long userId, Long groupId);
    void validateOwnerTransferable(Long requesterId, Long groupId, Long newOwnerId);
}
